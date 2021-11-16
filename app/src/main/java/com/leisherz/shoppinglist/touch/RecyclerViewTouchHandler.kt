package com.leisherz.todorecyclerview.touch

interface RecyclerViewTouchHandler {
 fun onDismissed(position: Int)
 fun onMove(fromPosition: Int, toPosition: Int)
}
