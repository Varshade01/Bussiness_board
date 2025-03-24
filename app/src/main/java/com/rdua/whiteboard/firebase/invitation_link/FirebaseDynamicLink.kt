package com.rdua.whiteboard.firebase.invitation_link

import android.net.Uri
import android.util.Log
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import javax.inject.Inject


    const val APP_PLAY_MARKET_LINK = "https://play.google.com/store/apps/details?id=com.instagram.android&hl=uk&gl=US"
    const val FIREBASE_APP_LINK = "https://rdua.page.link/"
    const val PACKAGE_NAME = "com.rdua.whiteboard"
    const val BOARDID_PREFIX = "&boardid="
    const val LOG_TAG = "DEEPLINK"

    class FirebaseDynamicLink @Inject constructor() : FirebaseDynamicLinkAPI {
        override fun generateDynamicLink(boardId: String) {
            FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(APP_PLAY_MARKET_LINK + BOARDID_PREFIX + boardId))
                .setDomainUriPrefix(FIREBASE_APP_LINK)
                .setAndroidParameters(
                    DynamicLink.AndroidParameters.Builder(PACKAGE_NAME)
                        .setFallbackUrl(Uri.parse(APP_PLAY_MARKET_LINK))
                        .build()
                )
                .buildShortDynamicLink()
                .addOnSuccessListener { result ->
                    val shortLink = result.shortLink
                    val flowChartLink = result.previewLink
                    Log.i(LOG_TAG, "Created short link:${shortLink.toString()}")
                    Log.i(LOG_TAG, "Created flowChartLink:${flowChartLink.toString()}")

//                val shortLinkWithBoardID = Uri.parse(shortLink.toString())
//                            .buildUpon()
//                            .appendQueryParameter("boardID", boardId)
//                            .build()
//
//                Log.i(LOG_TAG, "Created short link with boardID:${shortLinkWithBoardID.toString()}")
                }
                .addOnFailureListener{
                    Log.i(LOG_TAG, it.stackTraceToString())
                    //Failure
                }
//        generateShortDynamicLink(boardId)
        }
    }
