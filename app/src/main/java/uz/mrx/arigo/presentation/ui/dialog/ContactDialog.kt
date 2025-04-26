package uz.mrx.arigo.presentation.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import uz.mrx.arigo.databinding.DialogContactBinding

class ContactDialog : DialogFragment() {

    private var _binding: DialogContactBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.escImg.setOnClickListener {
            dismiss()
        }

        binding.contactCall.setOnClickListener {
            Toast.makeText(requireContext(), "Qo'ng'iroq qilindi", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        binding.contactTelegram.setOnClickListener {
            Toast.makeText(requireContext(), "Telegramga o'tildi", Toast.LENGTH_SHORT).show()
            dismiss()
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            // Dialogni to‘liq ekran qilish
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            window.setBackgroundDrawableResource(android.R.color.transparent) // Fonni shaffof qilish
            window.setWindowAnimations(android.R.style.Animation_Dialog) // Animatsiya qo‘shish (ixtiyoriy)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
