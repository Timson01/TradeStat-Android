package space.timur.tradestatandroid.ui.deal_detail

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import space.timur.tradestatandroid.R
import space.timur.tradestatandroid.databinding.FragmentAddEditDealBinding
import space.timur.tradestatandroid.databinding.FragmentDealDetailBinding

class DealDetailFragment : Fragment() {

    private lateinit var binding: FragmentDealDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDealDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}