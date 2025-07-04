package uz.mrx.arigo.presentation.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import uz.mrx.arigo.databinding.DialogRetryOrderBinding

class OrderDialogRetry(
    context: Context,
    private val onRetry: () -> Unit,
    private val onEdit: () -> Unit,
    private val onCancel: () -> Unit
) : Dialog(context) {

    private val binding = DialogRetryOrderBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)

        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }

        binding.esc.setOnClickListener {
            dismiss()
        }

        binding.yes.setOnClickListener {
            onEdit()
            dismiss()
        }

        binding.no.setOnClickListener {
            onCancel()
            dismiss()
        }

        binding.retry.setOnClickListener {
            onRetry()
            dismiss()
        }

    }
}
