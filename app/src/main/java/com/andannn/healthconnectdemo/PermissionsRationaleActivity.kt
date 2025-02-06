package com.andannn.healthconnectdemo


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.health.connect.client.PermissionController

class PermissionsRationaleActivity : ComponentActivity() {
    private val PERMISSION: MutableSet<String> = HashSet()
    private var requestPermissions: ActivityResultLauncher<Set<String>>? = null

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PERMISSION.add("android.permission.health.READ_STEPS")

        // Create the permissions launcher
        val requestPermissionActivityContract =
            PermissionController.createRequestPermissionResultContract()
//        requestPermissions = registerForActivityResult(
//            requestPermissionActivityContract,
//            object : ActivityResultCallback<Set<String?>?> {
//                override fun onActivityResult(permissions: Set<String>) {
//                    if (permissions.containsAll(PERMISSION)) {
//                        // Permissions successfully granted
//                        finish()
//                    } else {
//                        // Lack of required permissions
//                    }
//                }
//            })
//
//        binding.buttonRequestPermission.setOnClickListener(View.OnClickListener { showRationaleDialog() })
    }

    private fun showRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Health Permission Needed")
            .setMessage("We need access to your health data (e.g., step count) to provide personalized fitness advice and goals.")
            .setPositiveButton(
                "Allow"
            ) { dialog: DialogInterface?, which: Int ->
                // Request health-related permissions
                requestPermissions!!.launch(PERMISSION)
            }
            .setNegativeButton(
                "Deny"
            ) { dialog: DialogInterface?, which: Int ->
                // Handle the permission denial
//                finish()
            }
            .create()
            .show()
    }
}