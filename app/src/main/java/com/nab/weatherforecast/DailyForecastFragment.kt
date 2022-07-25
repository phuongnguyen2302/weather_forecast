package com.nab.weatherforecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nab.weatherforecast.databinding.FragmentDailyForecastBinding
import com.nab.weatherforecast.ui.DailyForecastViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
open class DailyForecastFragment : Fragment() {

    private var _binding: FragmentDailyForecastBinding? = null
    private val binding get() = _binding!!
    open lateinit var viewModel: DailyForecastViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = (activity?.application as DailyForecastApplication)
        viewModel = DailyForecastViewModel(application.dailyForecastUseCase, application.rxScheduler)

        _binding = FragmentDailyForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}