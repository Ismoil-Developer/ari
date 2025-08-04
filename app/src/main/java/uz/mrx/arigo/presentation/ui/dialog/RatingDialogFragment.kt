package uz.mrx.arigo.presentation.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.DialogFeedBackBinding

class RatingDialogFragment(
    private val comment: String,
    private val onSend: (rating: Int) -> Unit
) : DialogFragment() {

    private var _binding: DialogFeedBackBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogFeedBackBinding.inflate(LayoutInflater.from(context))

        val dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)

        // Transparent fon
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Bekor qilish
        binding.no.setOnClickListener {
            dialog.dismiss()
        }

        // Saqlash
        binding.save.setOnClickListener {
            val rating = binding.ratingBar.rating.toInt().coerceAtLeast(1)
            onSend.invoke(rating)
            dialog.dismiss()
        }

        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            window.setBackgroundDrawableResource(android.R.color.transparent)
            window.setGravity(Gravity.CENTER)
            window.setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
