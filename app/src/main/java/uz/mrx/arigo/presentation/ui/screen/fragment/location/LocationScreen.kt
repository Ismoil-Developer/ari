package uz.mrx.arigo.presentation.ui.screen.fragment.location

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.data.remote.request.location.LocationCreateRequest
import uz.mrx.arigo.databinding.ScreenLocationBinding
import uz.mrx.arigo.presentation.adapter.LocationAdapter
import uz.mrx.arigo.presentation.ui.dialog.LocationEdtDialog
import uz.mrx.arigo.presentation.ui.viewmodel.location.LocationScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.location.impl.LocationScreenViewModelImpl



@AndroidEntryPoint
class LocationScreen : Fragment(R.layout.screen_location) {

    private val binding: ScreenLocationBinding by viewBinding(ScreenLocationBinding::bind)

    private val viewModel: LocationScreenViewModel by viewModels<LocationScreenViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.containerAddLocation.setOnClickListener {
            viewModel.openAddLocationScreen(-1)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.openMainScreen()
            }
        })

        binding.btnContinueLn.setOnClickListener {
            viewModel.openMainScreen()
        }

        val adapter = LocationAdapter(
            onItemClickListener = { val lon = it.coordinates.coordinates[0]
                val lat = it.coordinates.coordinates[1]
                val wktCoordinates = "Point($lon $lat)"

                viewModel.postLocationIdActive(
                    it.id,
                    LocationCreateRequest(
                        custom_name = it.custom_name,
                        coordinates = wktCoordinates,
                        address = it.address,
                        active = true
                    )
                ) },
            edtClickListener = { location ->
                val dialog = LocationEdtDialog(
                    location = location,
                    onBackClick = { /* optional */ },
                    onDeleteClick = { deletedLocation ->
                        viewModel.deleteLocation(deletedLocation.id)
                    },
                    onEditClick = { id ->
                        viewModel.openAddLocationScreen(id) // ID ni yuborish
                    }
                )
                dialog.show(parentFragmentManager, "LocationEdtDialog")
            }
        )


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deleteLocation.collectLatest {
                if (it.status == "success delete") {
                    Toast.makeText(requireContext(), "O'chirildi", Toast.LENGTH_SHORT).show()
                    viewModel.getLocations() // faqat shu kerak
                }
            }
        }

        binding.icBack.setOnClickListener {
            viewModel.openMainScreen()
        }

        binding.rvLocation.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.postLocationActiveResponse.collectLatest {
                Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getLocation.collectLatest { locations ->
                adapter.submitList(locations)

                // Tanlangan (active = true) itemni avtomatik tanlash
                locations.find { it.active }?.let { adapter.setSelectedItem(it.id) }
            }
        }

    }
}
