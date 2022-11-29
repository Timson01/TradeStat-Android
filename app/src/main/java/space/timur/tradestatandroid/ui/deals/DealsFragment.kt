package space.timur.tradestatandroid.ui.deals

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import space.timur.tradestatandroid.R
import space.timur.tradestatandroid.data.Deal
import space.timur.tradestatandroid.data.SortOrder
import space.timur.tradestatandroid.databinding.FragmentDealsBinding
import space.timur.tradestatandroid.util.onQueryTextChanged
import java.text.DecimalFormat

@AndroidEntryPoint
class DealsFragment : Fragment(), DealsAdapter.OnItemClickListener {

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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dealsAdapter = DealsAdapter(this)

        binding.apply {
            rcViewDeals.apply {
                adapter = dealsAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            addDealButton.setOnClickListener {
                viewModel.onButtonAddDealClick()
            }

            // -- Changing Search Query Value --
            searchView.onQueryTextChanged {
                viewModel.searchQuery.value = it
            }

            tvDateResult.setOnClickListener {
                showDateRangePicker()
            }

            // --- On Swiped Delete Logic - Start ---
            ItemTouchHelper(object : ItemTouchHelper
            .SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val deal = dealsAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onDealSwiped(deal)
                }

            }).attachToRecyclerView(rcViewDeals)
            // --- On Swiped Delete Logic - End ---
        }




        // -- Init Start and End Date - Start --
        viewModel.defaultDeals.observe(viewLifecycleOwner) {
            viewModel.onStartDateSelected(it[it.size - 1].created)
            viewModel.onEndDateSelected(it[0].created)
        }
        // -- Init Start and End Date - End --

        // -- Display Information From Deals - Start --
        viewModel.deals.observe(viewLifecycleOwner) {
            dealsAdapter.submitList(it)
            if (it.isNotEmpty()) {
                binding.apply {
                    tvCountResult.text = it.size.toString()
                    var amount = 0.0
                    val df = DecimalFormat("#.##")
                    it.forEach {
                        amount += it.amount
                    }
                    if (amount < 0) {
                        tvIncomeResult.setTextColor(Color.RED)
                        incomeArrow.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.ic_down_arrow,
                                null
                            )
                        )
                    } else {
                        tvIncomeResult.setTextColor(Color.GREEN)
                        incomeArrow.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.ic_up_arrow,
                                null
                            )
                        )
                    }
                    tvIncomeResult.text = df.format(amount).toString()
                    tvDateResult.text =
                        "${it[it.size - 1].createdDateFormatted} - ${it[0].createdDateFormatted}"
                }
            }
        }
        // -- Display Information From Deals - End --

        setFragmentResultListener(
            "add_edit_request"
        ) {_, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.dealsEvent.collect { event ->
                when (event) {
                    is DealsViewModel.DealsEvent.NavigateToAddDealsScreen -> {
                        val action = DealsFragmentDirections.actionDealsFragmentToAddDealFragment()
                        findNavController().navigate(action)
                    }
                    is DealsViewModel.DealsEvent.ShowUndoDeleteMessage -> {
                        Snackbar.make(requireView(), "Task Deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO"){
                                viewModel.onUndoDeleteClick(event.deal)
                            }.show()
                    }
                    is DealsViewModel.DealsEvent.NavigateToEditDealsScreen -> {
                        val action = DealsFragmentDirections.actionDealsFragmentToAddDealFragment(event.deal)
                        findNavController().navigate(action)
                    }
                    is DealsViewModel.DealsEvent.ShowDealSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        // --- Read the Data from Preferences Flow ---

        initPopup()

    }

    // --- Date Range Picker - Start ---

    private fun showDateRangePicker() {
        val dateRangePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTitleText("Select Date")
            .build()
        dateRangePicker.show(parentFragmentManager, "date_range_picker")
        dateRangePicker.addOnPositiveButtonClickListener { datePicked ->
            viewModel.onStartDateSelected(datePicked.first)
            viewModel.onEndDateSelected(datePicked.second)
        }
    }

    // --- Date Range Picker - End ---

    // --- Popup Menu Initialization - Start ---

    private fun initPopup() {
        binding.apply {
            tvIncomeResult.setOnClickListener {
                val popupMenu = PopupMenu(context, it)
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_up -> {
                            viewModel.onSortOrderSelected(SortOrder.BY_POSITIVE_INCOME)
                            true
                        }
                        R.id.menu_down -> {
                            viewModel.onSortOrderSelected(SortOrder.BY_NEGATIVE_INCOME)
                            true
                        }
                        R.id.menu_all -> {
                            viewModel.onSortOrderSelected(SortOrder.BY_NAME_AND_DATE)
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.inflate(R.menu.menu_income_deals)
                try {
                    val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                    fieldMPopup.isAccessible = true
                    val mPopup = fieldMPopup.get(popupMenu)
                    mPopup.javaClass
                        .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                        .invoke(mPopup, true)
                } catch (e: Exception) {
                    Log.e("Main", "Error showing menu icons.", e)
                } finally {
                    popupMenu.show()
                }
            }

        }
    }
    // --- Popup Menu Initialization - End ---

    override fun onItemClick(deal: Deal) {
        viewModel.onDealSelected(deal)
    }

    override fun onItemLongClick(deal: Deal) {
        viewModel.onDealLongClick(deal)
    }
}