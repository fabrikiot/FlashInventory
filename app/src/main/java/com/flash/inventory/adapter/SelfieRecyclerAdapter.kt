package com.flash.inventory.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.flash.inventory.R

class SelfieRecyclerAdapter(
    val context: Context,
    val data: ArrayList<String>,
    val onClick: onClickItem
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val captureLayout = 0
    val imageLayout = 1

    class CaptureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: String, onClick: onClickItem) {

            val camera = itemView.findViewById<LinearLayout>(R.id.newTestChild1_camera)

            camera.setOnClickListener {

                onClick.onClick()

            }


        }

    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(context: Context, data: String, onClick: onClickItem, position: Int) {

            val image = itemView.findViewById<ImageView>(R.id.selfieChild2_image)
            val cross = itemView.findViewById<ImageView>(R.id.selfieChild2_cross)

            image.setImageURI(Uri.parse(data))

            cross.setOnClickListener {

                onClick.onClickCross(position)

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == captureLayout) {

            val v =
                LayoutInflater.from(parent.context).inflate(R.layout.selfie_child1, parent, false)
            return CaptureViewHolder(v)

        } else {

            val v =
                LayoutInflater.from(parent.context).inflate(R.layout.selfie_child2, parent, false)
            return ImageViewHolder(v)

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == captureLayout) {

            (holder as CaptureViewHolder).bind(data[position], onClick)

        } else {

            (holder as ImageViewHolder).bind(context, data[position], onClick, position)

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            captureLayout
        } else {
            imageLayout
        }
    }


    interface onClickItem {

        fun onClick()

        fun  onClickCross(pos: Int)

    }

}