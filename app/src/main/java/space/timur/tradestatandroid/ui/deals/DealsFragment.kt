package space.timur.tradestatandroid.ui.deals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import space.timur.tradestatandroid.R
import space.timur.tradestatandroid.databinding.FragmentDealsBinding
import javax.net.ssl.SSLSessionBindingEvent

class DealsFragment : Fragment() {

    private lateinit var binding: FragmentDealsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDealsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}