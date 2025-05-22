package uz.mrx.arigo.presentation.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import uz.mrx.arigo.databinding.DialogFindDeliveryBinding
import uz.mrx.arigo.presentation.ui.viewmodel.searchdelivery.SearchDeliveryScreenViewModel

class FindDeliveryDialog(private val viewModel: SearchDeliveryScreenViewModel) : DialogFragment() {

    private lateinit var binding: DialogFindDeliveryBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogFindDeliveryBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)

        binding.yes.setOnClickListener {
            viewModel.openOrderDeliveryScreen("")
            dismiss()
        }

        binding.no.setOnClickListener {
            dismiss()
        }

        return builder.create()
    }
}
