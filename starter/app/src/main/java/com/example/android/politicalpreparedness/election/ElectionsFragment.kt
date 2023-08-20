package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment : Fragment() {

    // TODO: Declare ViewModel
    private val viewModel: ElectionsViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = ElectionsViewModelFactory(application)
        ViewModelProvider(this, viewModelFactory)[ElectionsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // TODO: Add ViewModel values and create ViewModel
        val binding: FragmentElectionBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_election, container, false
        )
        binding.electionViewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.onSavedElectionS()
        val upcomingAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.onClickUpcomingElectionVoterInfo(election)
        })
        val savedAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.onClickUpcomingElectionVoterInfo(election)
        })
        binding.upcomingElectionRecycler.adapter = upcomingAdapter
        binding.saveElectionRecycler.adapter = savedAdapter
        viewModel.savedElectionList.observe(viewLifecycleOwner) {
            it?.let {
                savedAdapter.submitList(it)
            }
        }
        viewModel.navigateVoterInfoElection.observe(viewLifecycleOwner) { election ->
            election?.let {
                Navigation.findNavController(requireView()).navigate(
                    ElectionsFragmentDirections.electionsToVoterInfo(
                        election.id,
                        election.division,
                    )
                )
                viewModel.navigateToSaved()
            }
        }
        viewModel.upcomingElectionList.observe(viewLifecycleOwner) {
            it?.let {
                upcomingAdapter.submitList(it)
            }
        }
        return binding.root
    }

    // TODO: Refresh adapters when fragment loads
}