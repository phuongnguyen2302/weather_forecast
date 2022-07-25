package com.nab.weatherforecast.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nab.weatherforecast.DailyForecastApplication
import com.nab.weatherforecast.databinding.FragmentDailyForecastBinding

open class DailyForecastFragment : Fragment() {

    private var _binding: FragmentDailyForecastBinding? = null
    private val binding get() = _binding!!
    open var viewModel: DailyForecastViewModel? = null
    private lateinit var adapter: DailyForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyForecastBinding.inflate(inflater, container, false)

        val application = (activity?.application as DailyForecastApplication)
        if (viewModel == null) {
            viewModel =
                DailyForecastViewModel(application.dailyForecastUseCase, application.rxScheduler)
        }
        binding.viewModel = viewModel
        adapter = DailyForecastAdapter()

        setupAdapter()
        return binding.root
    }

    private fun setupAdapter() {
        binding.dailyForecastRecyclerView.adapter = adapter
        viewModel?.dailyForecastList?.observe(viewLifecycleOwner, adapter::submitList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        viewModel?.onPause()
    }
}