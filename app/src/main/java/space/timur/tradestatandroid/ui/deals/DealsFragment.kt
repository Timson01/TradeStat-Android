package space.timur.tradestatandroid.ui.deals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import space.timur.tradestatandroid.R
import space.timur.tradestatandroid.databinding.FragmentDealsBinding
import space.timur.tradestatandroid.ui.login.LogInFragmentDirections
import space.timur.tradestatandroid.ui.login.LoginViewModel
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.net.ssl.SSLSessionBindingEvent
import kotlin.math.roundToInt

@AndroidEntryPoint
class DealsFragment : Fragment() {

    private lateinit var binding: FragmentDealsBinding
    private val viewModel: DealsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDealsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dealsAdapter = DealsAdapter()

        binding.apply {
            rcViewDeals.apply {
                adapter = dealsAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            addDealButton.setOnClickListener {
                viewModel.onButtonAddDealClick()
            }
        }

        viewModel.deals.observe(viewLifecycleOwner){
            dealsAdapter.submitList(it)
            binding.apply {
                tvCountResult.text = it.size.toString()
                var amount = 0.0
                val df = DecimalFormat("#.##")
                it.forEach{
                    amount += it.amount
                }
                tvIncomeResult.text = df.format(amount).toString()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.dealsEvent.collect{ event ->
                when(event){
                    is DealsViewModel.DealsEvent.NavigateToAddDealsScreen -> {
                        val action = DealsFragmentDirections.actionDealsFragmentToAddDealFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }

    }

}