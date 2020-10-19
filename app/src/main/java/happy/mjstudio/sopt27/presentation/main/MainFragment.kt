package happy.mjstudio.sopt27.presentation.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ArcMotion
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import happy.mjstudio.sopt27.R
import happy.mjstudio.sopt27.databinding.FragmentMainBinding
import happy.mjstudio.sopt27.utils.AutoClearedValue
import happy.mjstudio.sopt27.utils.SimpleItemTouchHelperCallback
import happy.mjstudio.sopt27.utils.onDebounceClick
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainFragment : Fragment() {

    private var mBinding: FragmentMainBinding by AutoClearedValue()
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentMainBinding.inflate(inflater, container, false).let {
            mBinding = it
            it.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = viewModel

        setTransition()
        configureList()
        setFabClickListener()
        setLayoutChangeButtonsListener()
    }

    private fun setTransition() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 500L
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().getColor(R.color.colorTransparent))
        }
    }

    private fun configureList() = mBinding.list.run {
        adapter = SampleAdapter()
        ItemTouchHelper(SimpleItemTouchHelperCallback(viewModel)).attachToRecyclerView(this)
    }

    private fun setFabClickListener() = mBinding.fab onDebounceClick {
        showCard()
    }

    private fun setLayoutChangeButtonsListener() {
        mBinding.change onDebounceClick {
            hideListAndChangeAndShowAgain()
        }
        mBinding.cancel onDebounceClick {
            hideCard()
        }
    }

    private fun hideListAndChangeAndShowAgain() {
        hideList {
            changeListLayoutManager()
            showList()
            hideCard()
        }
    }

    private fun showCard() {
        TransitionManager.beginDelayedTransition(
            mBinding.root as ViewGroup, createContainerTransform(mBinding.fab, mBinding.card)
        )
        mBinding.card.isInvisible = false
        mBinding.fab.isInvisible = true
    }

    private fun hideCard() {
        TransitionManager.beginDelayedTransition(
            mBinding.root as ViewGroup, createContainerTransform(mBinding.card, mBinding.fab)
        )
        mBinding.card.isInvisible = true
        mBinding.fab.isInvisible = false
    }

    private fun createContainerTransform(startView: View, endView: View) = MaterialContainerTransform().apply {
        duration = 500L
        setPathMotion(ArcMotion())
        this.startView = startView
        this.endView = endView
        scrimColor = Color.TRANSPARENT
        setAllContainerColors(requireContext().getColor(R.color.colorSurface))
    }

    private fun hideList(endAction: () -> Unit) = mBinding.list.animate().alpha(0f).apply {
        duration = 300L
        withEndAction(endAction)
    }

    private fun showList() = mBinding.list.animate().alpha(1f).apply {
        duration = 300L
    }

    private fun changeListLayoutManager() {
        if (mBinding.list.layoutManager is GridLayoutManager) {
            mBinding.list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        } else {
            mBinding.list.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }
}