package space.timur.tradestatandroid.ui.deals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import space.timur.tradestatandroid.R
import space.timur.tradestatandroid.databinding.FragmentDealsBinding
import javax.net.ssl.SSLSessionBindingEvent

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
        }

        viewModel.deals.observe(viewLifecycleOwner){
            dealsAdapter.submitList(it)
        }

    }

}