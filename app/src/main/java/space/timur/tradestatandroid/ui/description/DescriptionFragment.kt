package space.timur.tradestatandroid.ui.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collect
import space.timur.tradestatandroid.databinding.FragmentDescriptionBinding

class DescriptionFragment : Fragment() {

    private lateinit var binding: FragmentDescriptionBinding
    private val viewModel: DescriptionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDescriptionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonNext.setOnClickListener{
                viewModel.onButtonNextClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.descEvent.collect {
                event ->
                when(event) {
                    is DescriptionViewModel.DescEvent.NavigateToWelcomeScreen -> {
                        val action = DescriptionFragmentDirections.actionDescriptionFragmentToWelcomeFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }
}