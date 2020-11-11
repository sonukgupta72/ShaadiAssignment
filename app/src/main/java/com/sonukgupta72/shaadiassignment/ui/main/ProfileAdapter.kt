package com.sonukgupta72.shaadiassignment.ui.main

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sonukgupta72.shaadiassignment.R
import com.sonukgupta72.shaadiassignment.db.DataModel
import com.sonukgupta72.shaadiassignment.network.ProfileResponseDataModule
import kotlinx.android.synthetic.main.item_profile.view.*

class ProfileAdapter constructor(private val list: List<DataModel>,
                                 private val clickListener: ItemOnclickListener):
    RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    inner class ViewHolder constructor(val view: View): RecyclerView.ViewHolder(view) {

        private val profileImage= view.profile_image
        private val tvName= view.tv_name
        private val clResponse= view.cl_response
        private val clDecline= view.cl_decline
        private val clAccept= view.cl_accept
        private val tvResponseMessage= view.tv_response_message

        init {
            clDecline.setOnClickListener {
                clickListener.onDecline(list[adapterPosition], adapterPosition)
            }
            clAccept.setOnClickListener {
                clickListener.onAccept(list[adapterPosition], adapterPosition)
            }
        }

        fun bindDataWithView(dataModel: DataModel) {
            val profileData = dataModel.profileInfo
            tvName.text = "${profileData.name?.title} ${profileData.name?.first} ${profileData.name?.last}"
            Glide.with(view.context).load(profileData.picture.large).into(profileImage)

            if (!TextUtils.isEmpty(dataModel.responseMessage)) {
                tvResponseMessage.visibility = View.VISIBLE
                clResponse.visibility = View.GONE

                tvResponseMessage.text = dataModel.responseMessage
            } else {
                tvResponseMessage.visibility = View.GONE
                clResponse.visibility = View.VISIBLE
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_profile, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //To change body of created functions use File | Settings | File Templates.
        holder.bindDataWithView(list[holder.adapterPosition])
    }

    interface ItemOnclickListener {
        fun onAccept(item: DataModel, position: Int)
        fun onDecline(item: DataModel, position: Int)
    }
}