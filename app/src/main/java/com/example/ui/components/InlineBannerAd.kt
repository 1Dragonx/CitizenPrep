package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CivicGoldSecondary
import com.example.ui.theme.CivicNavyPrimary

/**
 * AdMob Inline Banner Advertisement component per Mini PRD.
 * Ad Unit ID: ca-app-pub-3940256099942544/9214589741
 * Clearly labeled as "ADVERTISEMENT", responsive, non-obstructive, with impression/click tracking.
 */
object AdManager {
    const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741"

    var isAdsEnabled: Boolean = true
    var totalImpressions: Int = 0
    var totalClicks: Int = 0

    fun recordImpression() {
        totalImpressions++
    }

    fun recordClick() {
        totalClicks++
    }

    fun getCtr(): Float {
        return if (totalImpressions > 0) {
            (totalClicks.toFloat() / totalImpressions.toFloat()) * 100f
        } else 0f
    }
}

@Composable
fun InlineBannerAd(
    modifier: Modifier = Modifier,
    placement: String = "general",
    enabled: Boolean = AdManager.isAdsEnabled,
    onAdClicked: (() -> Unit)? = null
) {
    if (!enabled) return

    var isDismissed by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }

    LaunchedEffect(placement) {
        AdManager.recordImpression()
    }

    AnimatedVisibility(visible = !isDismissed) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .testTag("inline_banner_ad_$placement")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Top Meta Header: ADVERTISEMENT label & close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                            modifier = Modifier.padding(end = 6.dp)
                        ) {
                            Text(
                                text = "ADVERTISEMENT",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = "Sponsored • Ad ID: ...9741",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Ad Information",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier
                                .size(14.dp)
                                .clickable { showInfoDialog = !showInfoDialog }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss Ad",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier
                                .size(14.dp)
                                .clickable { isDismissed = true }
                                .testTag("dismiss_ad_button")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Ad Creative Content
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable {
                            AdManager.recordClick()
                            onAdClicked?.invoke()
                        }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = CivicGoldSecondary.copy(alpha = 0.15f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = CivicGoldSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Citizenship Legal Aid & Language Tutoring",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            maxLines = 1
                        )
                        Text(
                            text = "Affordable naturalization consultations & certified translators.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.testTag("ad_cta_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Learn More",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
