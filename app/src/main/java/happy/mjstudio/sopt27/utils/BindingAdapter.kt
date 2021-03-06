package happy.mjstudio.sopt27.utils

import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import happy.mjstudio.sopt27.R

@BindingAdapter("app:url", requireAll = false)
fun ImageView.loadUrlAsync(url: String?) {
    val anim = CircularProgressDrawable(context).apply {
        strokeWidth = 4f
        setColorSchemeColors(
            *listOf(
                R.color.colorPrimary, R.color.colorSecondary, R.color.colorError70
            ).map { context.getColor(it) }.toIntArray()
        )
        start()
    }

    if (url == null) {
        Glide.with(this).load(anim).into(this)
    } else {
        Glide.with(this).load(url)
            .transition(withCrossFade(DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
            .placeholder(anim).into(this)
    }
}

@BindingAdapter("app:useCircleOutlineWithRadius")
fun ShapeableImageView.useCircleOutlineWithRadius(radius: Float) {
    shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(radius)
}

@BindingAdapter("android:visibility")
fun View.setVisibilityBinding(isVisible: Boolean) {
    this.isVisible = isVisible
}

@BindingAdapter("app:selected_binding")
fun View.setSelectedBinding(isSelected: Boolean) {
    this.isSelected = isSelected
}