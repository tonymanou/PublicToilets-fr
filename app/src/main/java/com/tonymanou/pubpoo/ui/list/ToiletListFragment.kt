package com.tonymanou.pubpoo.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tonymanou.pubpoo.R
import com.tonymanou.pubpoo.databinding.ListFragmentBinding
import com.tonymanou.pubpoo.model.Toilet
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@OptIn(InternalCoroutinesApi::class)
class ToiletListFragment : Fragment() {

    companion object {
        fun newInstance() = ToiletListFragment()
    }

    private val adapter = ToiletsAdapter()

    private val viewModel by viewModels<ToiletListViewModel> {
        ToiletListViewModel.factory(this, arguments)
    }

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ListFragmentBinding.bind(view)

        binding.toiletsList.also {
            it.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            it.adapter = adapter
        }

        viewModel.accessibleOnly.observe(viewLifecycleOwner) {
            binding.toiletsInverseAccessibilityFilter.updateFilter(it)
            searchToilets(it)
        }

        // Scroll to top when the list is refreshed from network
        lifecycleScope.launch {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect(object : FlowCollector<Any> {
                    override suspend fun emit(value: Any) {
                        binding.toiletsList.scrollToPosition(0)
                    }
                })
        }
    }

    private fun FloatingActionButton.updateFilter(accessibleOnly: Boolean) {
        val context = context
        if (accessibleOnly) {
            setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_accessible))
            contentDescription = context.getString(R.string.toilet_accessible_filter_description)
        } else {
            setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_not_accessible))
            contentDescription = context.getString(R.string.toilet_not_accessible_filter_description)
        }
        setOnClickListener {
            searchToilets(!accessibleOnly)
        }
    }

    private fun searchToilets(accessibleOnly: Boolean) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchToilets(accessibleOnly)
                .collect(object : FlowCollector<PagingData<Toilet>> {
                    override suspend fun emit(value: PagingData<Toilet>) {
                        adapter.submitData(value)
                    }
                })
        }
    }
}
