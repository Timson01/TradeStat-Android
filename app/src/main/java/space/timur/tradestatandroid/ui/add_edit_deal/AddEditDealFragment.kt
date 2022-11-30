package space.timur.tradestatandroid.ui.add_edit_deal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_deals.*
import space.timur.tradestatandroid.databinding.FragmentAddEditDealBinding
import space.timur.tradestatandroid.util.exhaustive

@AndroidEntryPoint
class AddEditDealFragment : Fragment() {

    private lateinit var binding: FragmentAddEditDealBinding
    private val viewModel: AddEditDealViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditDealBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            editTickerName.setText(viewModel.dealName)
            editDescription.setText(viewModel.dealDescription)
            tvEditDate.text = viewModel.dealCreated
            editHashtag.setText(viewModel.dealHashtag)
            amountOfDeal.setText(viewModel.dealAmount.toString())
            numberOfStocks.setText(viewModel.dealNumberOfStocks.toString())
            fragTitle.text = viewModel.fragmentTitle

            editTickerName.addTextChangedListener {
                viewModel.dealName = it.toString()
            }
            editDescription.addTextChangedListener {
                viewModel.dealDescription = it.toString()
            }
            editHashtag.addTextChangedListener {
                viewModel.dealHashtag = it.toString()
            }
            tvEditDate.addTextChangedListener {
                viewModel.dealCreated = it.toString()
            }
            amountOfDeal.addTextChangedListener {
                viewModel.dealAmount = it.toString().toDouble()
            }
            numberOfStocks.addTextChangedListener {
                viewModel.dealNumberOfStocks = it.toString().toInt()
            }

            btnCreateDeal.setOnClickListener{
               viewModel.onSaveClick()
            }

            btnBack.setOnClickListener {
                viewModel.onBackClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditDealEvent.collect{ event ->
                when(event){
                    is AddEditDealViewModel.AddEditDealEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is AddEditDealViewModel.AddEditDealEvent.NavigateBackWithResult -> {
                        binding.editTickerName.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf(
                                "add_edit_result" to event.result
                            )
                        )
                        findNavController().popBackStack()
                    }
                    AddEditDealViewModel.AddEditDealEvent.NavigateBack -> {
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }
    }

}