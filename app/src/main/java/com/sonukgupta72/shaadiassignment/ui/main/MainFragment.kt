package com.sonukgupta72.shaadiassignment.ui.main

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sonukgupta72.shaadiassignment.R
import com.sonukgupta72.shaadiassignment.db.DataModel
import com.sonukgupta72.shaadiassignment.network.ProfileResponseDataModule
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var progressBar: ProgressBar? = null
    private val profiles: MutableList<DataModel> = ArrayList()
    private var profileAdapter: ProfileAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        initView()
        getProfileData()
    }

    private fun initView() {
        val layout: RelativeLayout = RelativeLayout(context)
        progressBar = ProgressBar(context)
        val params = RelativeLayout.LayoutParams(100, 100)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        layout.addView(progressBar, params)

        rv_profiles.layoutManager = LinearLayoutManager(context)
        profileAdapter = ProfileAdapter(profiles, object : ProfileAdapter.ItemOnclickListener{
            override fun onAccept(item: DataModel, position: Int) {
                viewModel.updateProfile(item, "Accepted")
            }

            override fun onDecline(item: DataModel, position: Int) {
                viewModel.updateProfile(item, "Rejected")
            }

        })
        rv_profiles.adapter = profileAdapter
    }

    private fun getProfileData() {
        viewModel.getProfiles()

        viewModel.getResultModel()?.observe(viewLifecycleOwner, Observer<List<DataModel>?> {
            setRecyclerViewData(it)
        })

        viewModel.getError().observe(viewLifecycleOwner, Observer<String> {
            onApiFail(it)
        })

        viewModel.getProgress().observe(viewLifecycleOwner, Observer<Boolean> {
        })
    }

    private fun onApiFail(it: String?) {
            if (!TextUtils.isEmpty(it)) {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
    }

    private fun setRecyclerViewData(list: List<DataModel>?){
        list?.run {
            profiles.clear()
            profiles.addAll(this)
            profileAdapter?.notifyDataSetChanged()
            Toast.makeText(context, "List Refreshed!", Toast.LENGTH_LONG).show()
        }
    }

}
