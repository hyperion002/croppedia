package com.example.croppedia.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.croppedia.databinding.FragmentHomeBinding
import com.example.croppedia.ui.adapter.CropAdapter
import com.example.croppedia.ui.viewmodel.MainViewModel
import com.example.croppedia.utils.NetworkListener
import com.example.croppedia.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        const val TAG = "HomeFragment"
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var dataRequested = false

    private lateinit var mainViewModel: MainViewModel
    private lateinit var networkListener: NetworkListener

    private val cropAdapter by lazy { CropAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setUpRecyclerView()

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    readDatabase()
                }
        }

        return binding.root
    }

    private fun readDatabase() {
        Log.d(TAG, "readDatabase called!")
        lifecycleScope.launch {
            mainViewModel.readCrops.observe(viewLifecycleOwner) { crops ->
                if (crops.isNotEmpty() && dataRequested) {
                    Log.d(TAG, "Data loaded from Database")
                    cropAdapter.setData(crops)
                    hideShimmerEffect()
                } else {
                    if (!dataRequested) {
                        Log.d(TAG, "Data requested from API")
                        requestApiData()
                        dataRequested = true
                    }
                }
            }
        }
    }

    private fun requestApiData() {
        Log.d(TAG, "requestApiData called!")
        mainViewModel.getAllCrops()
        mainViewModel.getAllCropsResponse.observe(viewLifecycleOwner) { response ->
            when(response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.crops?.let { cropAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        }
    }

    private fun loadDataFromCache() {
        Log.d(TAG, "loadDataFromCache called!")
        lifecycleScope.launch {
            mainViewModel.readCrops.observe(viewLifecycleOwner) { hashiras ->
                if (hashiras.isNotEmpty()) {
                    cropAdapter.setData(hashiras)
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = cropAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}