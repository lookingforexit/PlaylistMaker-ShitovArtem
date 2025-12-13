@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.playlistmaker.ui.screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.playlistmaker.R

@Composable
fun SettingsScreen(onClick: () -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            modifier = Modifier,
            title = {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = stringResource(R.string.settings),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            navigationIcon = {
                Icon(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(32.dp)
                        .clickable { onClick() },
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsElement(
            text = stringResource(R.string.dark_theme),
            onClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                }
                context.startActivity(intent)
            }
        )

        SettingsElement(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = stringResource(R.string.share),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            text = stringResource(R.string.share),
            onClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                }
                context.startActivity(Intent.createChooser(intent, null))
            }
        )

        SettingsElement(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_helper),
                    contentDescription = stringResource(R.string.support),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            text = stringResource(R.string.support),
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:".toUri()
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.my_email)))
                    putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.msg_to_devs))
                    putExtra(Intent.EXTRA_TEXT, context.getString(R.string.greet_for_devs))
                }
                context.startActivity(Intent.createChooser(intent, null))
            }
        )

        SettingsElement(
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.user_agreement),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            text = stringResource(R.string.user_agreement),
            onClick = {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    context.getString(R.string.yandex_courses_offer).toUri()
                )
                context.startActivity(browserIntent)
            }
        )
    }
}

@Composable
fun SettingsElement(
    icon: @Composable () -> Unit,
    text: String,
    onClick: () -> Unit = { }
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 20.dp),
                text = text,
                fontSize = 20.sp
            )
            Box(modifier = Modifier.padding(end = 16.dp, top = 20.dp)) {
                icon()
            }
        }
    }
}

@Composable
fun SettingsElement(
    text: String,
    onClick: () -> Unit = { }
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 20.dp),
                text = text,
                fontSize = 20.sp
            )
            var checked by rememberSaveable { mutableStateOf(false) }
            Switch(
                modifier = Modifier.padding(end = 12.dp, top = 14.dp),
                checked = checked,
                onCheckedChange = {
                    checked = it
                },
            )
        }
    }
}