package com.adammcneilly.wear.playground.tile

import android.content.Context
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.material.CircularProgressIndicator
import androidx.wear.protolayout.material.Colors
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.Typography
import androidx.wear.protolayout.material.layouts.EdgeContentLayout
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.tooling.preview.Preview
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService

private const val RESOURCES_VERSION = "0"

/**
 * Skeleton for a tile with no images.
 */
@OptIn(ExperimentalHorologistApi::class)
class MainTileService : SuspendingTileService() {
    override suspend fun resourcesRequest(requestParams: RequestBuilders.ResourcesRequest) = resources()

    override suspend fun tileRequest(requestParams: RequestBuilders.TileRequest): TileBuilders.Tile {
        println("ADAMLOG - TILE REQUEST: $requestParams")

        return tile(requestParams, this)
    }
}

private fun resources(): ResourceBuilders.Resources =
    ResourceBuilders.Resources
        .Builder()
        .setVersion(RESOURCES_VERSION)
        .build()

private fun tile(
    requestParams: RequestBuilders.TileRequest,
    context: Context,
): TileBuilders.Tile {
    val singleTileTimeline = TimelineBuilders.Timeline
        .Builder()
        .addTimelineEntry(
            TimelineBuilders.TimelineEntry
                .Builder()
                .setLayout(
                    LayoutElementBuilders.Layout
                        .Builder()
                        .setRoot(tileLayout(requestParams, context))
                        .build(),
                ).build(),
        ).build()

    return TileBuilders.Tile
        .Builder()
        .setResourcesVersion(RESOURCES_VERSION)
        .setTileTimeline(singleTileTimeline)
        .build()
}

@Suppress("MagicNumber")
private fun tileLayout(
    requestParams: RequestBuilders.TileRequest,
    context: Context,
): LayoutElementBuilders.LayoutElement =
    EdgeContentLayout
        .Builder(requestParams.deviceConfiguration)
        .setResponsiveContentInsetEnabled(true)
        .setEdgeContent(
            CircularProgressIndicator
                .Builder()
                .setProgress(0.75F)
                .build(),
        ).setContent(
            LayoutElementBuilders.Column
                .Builder()
                .addContent(
                    Text
                        .Builder(context, "Upcoming Events")
                        .setColor(argb(Colors.DEFAULT.onSurface))
                        .setTypography(Typography.TYPOGRAPHY_BODY1)
                        .build(),
                ).addContent(
                    CompactChip
                        .Builder(
                            context,
                            "Click Me",
                            ModifiersBuilders.Clickable
                                .Builder()
                                .setId("ActivityLaunch")
                                .setOnClick(
                                    ActionBuilders.LaunchAction
                                        .Builder()
                                        .setAndroidActivity(
                                            ActionBuilders.AndroidActivity
                                                .Builder()
                                                .setPackageName("com.adammcneilly.wear.playground")
                                                .setClassName(
                                                    "com.adammcneilly.wear.playground.presentation.MainActivity",
                                                ).addKeyToExtraMapping(
                                                    "TILE_BUTTON",
                                                    ActionBuilders.stringExtra("1234"),
                                                ).build(),
                                        ).build(),
                                ).build(),
                            DeviceParametersBuilders.DeviceParameters
                                .Builder()
                                .build(),
                        ).build(),
                ).build(),
        ).build()

@Preview(device = WearDevices.SMALL_ROUND)
@Preview(device = WearDevices.LARGE_ROUND)
fun tilePreview(context: Context) =
    TilePreviewData(::resources) {
        tile(it, context)
    }
