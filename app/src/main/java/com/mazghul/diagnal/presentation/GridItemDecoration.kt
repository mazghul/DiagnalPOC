package com.mazghul.diagnal.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


//class SpacesItemDecoration(private val space: Int) : ItemDecoration() {
//    override fun getItemOffsets(
//        outRect: Rect, view: View?,
//        parent: RecyclerView, state: RecyclerView.State?
//    ) {
//        outRect.left = space
//        outRect.right = space
//        outRect.bottom = space
//
//        // Add top margin only for the first item to avoid double space between items
//        if (parent.getChildLayoutPosition(view) == 0) {
//            outRect.top = space
//        } else {
//            outRect.top = 0
//        }
//    }
//
//}

class GridItemDecoration(
    val spacing: Int,
    private val spanCount: Int,
    private val includeEdge: Boolean
) :
    RecyclerView.ItemDecoration() {

    /**
     * Applies padding to all sides of the [Rect], which is the container for the view
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column
        if (includeEdge) {
            outRect.left =
                spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right =
                (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = 90 // item bottom
        } else {
            outRect.left =
                column * spacing / spanCount // column * ((1f / spanCount) * spacing)
            outRect.right =
                spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
        }
    }
}