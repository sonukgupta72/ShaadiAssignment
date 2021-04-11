package com.sonukgupta72.shaadiassignment.ui.main

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import com.sonukgupta72.shaadiassignment.corutine.Status
import com.sonukgupta72.shaadiassignment.databinding.MainFragmentBinding
import com.sonukgupta72.shaadiassignment.db.DataModel
import com.sonukgupta72.shaadiassignment.network.ProfileResponseDataModule
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var progressBar: ProgressBar? = null
    private val profiles: MutableList<DataModel> = ArrayList()
    private var profileAdapter: ProfileAdapter? = null
    private lateinit var binding: MainFragmentBinding

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View {
//
//        binding = MainFragment.inflate(, container, false)
//        return binding
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragmentBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        initView()
        getProfileData()
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//    }

    private fun initView() {
        val layout: RelativeLayout = RelativeLayout(context)
        progressBar = ProgressBar(context)
        val params = RelativeLayout.LayoutParams(100, 100)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        layout.addView(progressBar, params)

        binding.rvProfiles.layoutManager = LinearLayoutManager(context)
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
        viewModel.getProfilesCr().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users -> viewModel.onSuccess(users) }
                    }
                    Status.ERROR -> {
//                        recyclerView.visibility = View.VISIBLE
//                        progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.msg, Toast.LENGTH_LONG).show()
                        Log.d("OkHttp", it.msg ?: "Error Occurred!")
                    }
                    Status.LOADING -> {
//                        progressBar.visibility = View.VISIBLE
//                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })

        viewModel.getResultModel()?.observe(viewLifecycleOwner, Observer<List<DataModel>?> {
            setRecyclerViewData(it)
        })

//        viewModel.getError().observe(viewLifecycleOwner, Observer<String> {
//            onApiFail(it)
//        })
//
//        viewModel.getProgress().observe(viewLifecycleOwner, Observer<Boolean> {
//        })
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
