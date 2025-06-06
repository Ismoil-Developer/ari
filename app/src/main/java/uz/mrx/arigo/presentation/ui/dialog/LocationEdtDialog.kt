

package uz.mrx.arigo.presentation.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.mrx.arigo.data.remote.response.location.LocationCreateResponse
import uz.mrx.arigo.databinding.DialogLocationEdtBinding

class LocationEdtDialog(
    private val location: LocationCreateResponse,
    private val onBackClick: () -> Unit,
    private val onDeleteClick: (LocationCreateResponse) -> Unit,
    private val onEditClick: (Int) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: DialogLocationEdtBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        _binding = DialogLocationEdtBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        setupUI()

        return dialog
    }

    private fun setupUI() = with(binding) {
        yourLocation.text = location.address
        locationTxt.text = location.custom_name

        back.setOnClickListener {
            onBackClick.invoke()
            dismiss()
        }

        delete.setOnClickListener {
            onDeleteClick.invoke(location)
            dismiss()
        }

        icEdt.setOnClickListener {
            onEditClick.invoke(location.id)
            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
