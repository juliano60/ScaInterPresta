package com.nanoporetech.scainternew.ui.screens.consultation

import android.Manifest
import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.nanoporetech.scainternew.model.BarScanState
import com.nanoporetech.scainternew.ui.utils.BarcodeAnalyzer
import java.util.concurrent.Executor

fun isEmulator(): Boolean =
    android.os.Build.FINGERPRINT.contains("generic") ||
            android.os.Build.MODEL.contains("Emulator") ||
            android.os.Build.MODEL.contains("Android SDK built for x86")


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CodeScannerScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var scanState by remember { mutableStateOf<BarScanState>(BarScanState.Idle) }
    var lastValue by remember { mutableStateOf<String?>(null) }
    val permission = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        if (!permission.status.isGranted) permission.launchPermissionRequest()
    }

    Box(modifier) {
        if (permission.status.isGranted) {
            CameraPreview(
                context = context,
                lifecycleOwner = lifecycleOwner,
                onBarcode = { value ->
                    // de-dupe so it doesn't fire 30 times
                    if (value != lastValue) {
                        lastValue = value
                        scanState = BarScanState.ScanSuccess(value)
                    }
                },
                onError = { msg ->
                    scanState = BarScanState.Error(msg)
                }
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Camera permission is required to scan barcodes.")
                Spacer(Modifier.height(12.dp))
                Button(onClick = { permission.launchPermissionRequest() }) {
                    Text("Grant permission")
                }
            }
        }

        // Simple overlay UI based on scan state
        when (val s = scanState) {
            BarScanState.Idle -> OverlayHint("Position the barcode in front of the camera.")
            BarScanState.Loading -> OverlayLoading("Scanning…")
            is BarScanState.ScanSuccess -> OverlaySuccess(
                value = s.value,
                onDone = {
                    // reset and keep scanning
                    scanState = BarScanState.Idle
                    lastValue = null
                }
            )
            is BarScanState.Error -> OverlayError(s.error)
        }
    }
}

@Composable
private fun OverlayHint(text: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Surface(tonalElevation = 6.dp, modifier = Modifier.padding(16.dp)) {
            Text(text, modifier = Modifier.padding(12.dp))
        }
    }
}

@Composable
private fun OverlayLoading(text: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(Modifier.height(12.dp))
            Text(text)
        }
    }
}

@Composable
private fun OverlaySuccess(value: String, onDone: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Surface(tonalElevation = 6.dp, modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Scanned: $value", modifier = Modifier.weight(1f))
                Spacer(Modifier.width(12.dp))
                Button(onClick = onDone) { Text("Done") }
            }
        }
    }
}

@Composable
private fun OverlayError(msg: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Surface(color = MaterialTheme.colorScheme.errorContainer) {
            Text("Error: $msg", modifier = Modifier.padding(12.dp))
        }
    }
}

@Composable
private fun CameraPreview(
    context: Context,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    onBarcode: (String) -> Unit,
    onError: (String) -> Unit,
) {
    val cameraExecutor = remember { ContextCompat.getMainExecutor(context) as Executor } // analyzer can also use a bg executor
    val analysisExecutor = remember { java.util.concurrent.Executors.newSingleThreadExecutor() }

    DisposableEffect(Unit) {
        onDispose { analysisExecutor.shutdown() }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        },
        update = { previewView ->
            val providerFuture = ProcessCameraProvider.getInstance(context)
            providerFuture.addListener({
                val cameraProvider = providerFuture.get()

                val preview = androidx.camera.core.Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val analysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(
                            analysisExecutor,
                            BarcodeAnalyzer(onBarcode = onBarcode)
                        )
                    }

                val selector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(lifecycleOwner, selector, preview, analysis)
                } catch (t: Throwable) {
                    onError(t.message ?: "Failed to bind camera use cases")
                }
            }, cameraExecutor)
        }
    )
}