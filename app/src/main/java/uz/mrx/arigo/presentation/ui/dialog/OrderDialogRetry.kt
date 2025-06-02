package uz.mrx.arigo.presentation.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import uz.mrx.arigo.databinding.DialogRetryOrderBinding

class OrderDialogRetry(
    context: Context,
    private val onRetry: () -> Unit
) : Dialog(context) {

    private val binding = DialogRetryOrderBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)
        setCancelable(false)

        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }

        binding.yes.setOnClickListener {
            onRetry()
            dismiss()
        }

        binding.no.setOnClickListener {
            dismiss()
        }
    }
}
