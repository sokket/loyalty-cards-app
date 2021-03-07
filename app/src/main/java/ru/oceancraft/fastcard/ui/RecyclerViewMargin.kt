package ru.oceancraft.fastcard.ui

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class RecyclerViewMargin(
    context : Context,
    marginInDp: Int,
    private val columns: Int
) : ItemDecoration() {

    private val marginInPx : Int

    init {
        val metrics = context.resources.displayMetrics
        marginInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            marginInDp.toFloat(),
            metrics
        ).toInt()
    }

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        outRect.right = marginInPx
        outRect.bottom = marginInPx
        if (position < columns)
            outRect.top = marginInPx
        if (position % columns == 0)
            outRect.left = marginInPx
    }

}