package com.example.coinhakodemo.module.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coinhakodemo.base.BaseFragment
import com.example.coinhakodemo.databinding.FragmentHomeBinding
import com.example.coinhakodemo.model.BaseDataState
import com.example.coinhakodemo.model.home.CoinResponse
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@InternalCoroutinesApi
class HomeFragment : BaseFragment() {

    private val viewModel by viewModel<HomeViewModel>()
    private var adapter: CoinAdapter? = null

    private var binding: FragmentHomeBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadData()
                viewModel.coinsState.collect {
                    when (it) {
                        BaseDataState.Loading -> shouldLoading(true)
                        is BaseDataState.Success<*> -> {

                            (it.data as? List<CoinResponse>)?.let {
                                showData(it)
                            }
                            shouldLoading(false)
                        }
                        is BaseDataState.Error -> {
                            shouldLoading(false)
                            //todo handle show error
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.fhEdtSearch?.addTextChangedListener {
            viewModel.onInputSearchChanged(it.toString())
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopFetchData()
    }

    private fun showData(listCoin: List<CoinResponse>) {
        if (adapter == null) {
            adapter = CoinAdapter()
            binding?.fhListCoin?.layoutManager = LinearLayoutManager(requireContext())
            binding?.fhListCoin?.adapter = adapter

        }
        adapter?.setData(listCoin)
    }


    private fun shouldLoading(isShow: Boolean) {
        //--just show loading for the first time
        binding?.fhLoading?.visibility =
            if (isShow && (adapter?.itemCount ?: 0) == 0) View.VISIBLE else View.GONE
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}