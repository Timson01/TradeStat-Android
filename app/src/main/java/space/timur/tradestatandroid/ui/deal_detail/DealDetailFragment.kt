package space.timur.tradestatandroid.ui.deal_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import space.timur.tradestatandroid.databinding.FragmentDealDetailBinding
import space.timur.tradestatandroid.util.exhaustive
import java.text.SimpleDateFormat

class DealDetailFragment : Fragment() {

    private lateinit var binding: FragmentDealDetailBinding
    private val viewModel: DealDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDealDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            editTickerName.setText(viewModel.dealName)
            editDescription.setText(viewModel.dealDescription)
            tvEditDate.text = SimpleDateFormat("dd.MM.yy").format(viewModel.dealCreated)
            editHashtag.setText(viewModel.dealHashtag)
            amountOfDeal.setText(viewModel.dealAmount.toString())
            numberOfStocks.setText(viewModel.dealNumberOfStocks.toString())

            btnDetailBack.setOnClickListener {
                viewModel.onBackClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.dealDetailEvent.collect{ event ->
                when(event){
                    is DealDetailViewModel.DealDetailEvent.NavigateBack -> {
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }
    }
}