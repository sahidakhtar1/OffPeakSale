package com.appsauthority.appwiz.custom;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.appsauthority.appwiz.ImageZoomActivity;
import com.appsauthority.appwiz.models.MediaObject;
import com.appsauthority.appwiz.utils.ImageCacheLoader;
import com.appsauthority.appwiz.utils.Utils;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.offpeaksale.consumer.R;

public class MediaView extends LinearLayout implements OnInitializedListener {

	MediaObject mediaObject;

	RelativeLayout frameVideoView, frameImageView, frameYoutubeView;

	VideoView videoView;
	ImageView imageView;
	WebView gifWebview;
	YouTubePlayerView youtubeplayerview;
	Context context;
	ImageCacheLoader imageCacheloader;
	YouTubePlayer youtubePlayer;
	YouTubePlayerSupportFragment youtubeplayerFragment;
	Boolean isFragment;

	public MediaView(Context context, MediaObject mediaObject,
			Boolean isFragment) {
		super(context);
		this.context = context;
		this.mediaObject = mediaObject;
		imageCacheloader = new ImageCacheLoader(context);
		this.isFragment = isFragment;
		init();

		// TODO Auto-generated constructor stub
	}

	void init() {

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.home_banner_layout, null, false);
		this.addView(view);

		frameVideoView = (RelativeLayout) view
				.findViewById(R.id.frameVideoView);
		frameImageView = (RelativeLayout) view
				.findViewById(R.id.frameImageView);
		frameYoutubeView = (RelativeLayout) view
				.findViewById(R.id.frameYoutubeView);

		videoView = (VideoView) view.findViewById(R.id.videoView);

		imageView = (ImageView) view.findViewById(R.id.imageView);
		gifWebview = (WebView) view.findViewById(R.id.gifWebview);

		if (mediaObject.fileType.equalsIgnoreCase("image")) {
			if (mediaObject.filePath.endsWith("gif")) {
				gifWebview.setVisibility(View.VISIBLE);
				imageView.setVisibility(View.GONE);
				loadGifImage();
				// gifWebview.setOnTouchListener(new View.OnTouchListener() {
				//
				// @Override
				// public boolean onTouch(View arg0, MotionEvent arg1) {
				// // TODO Auto-generated method stub
				// if (arg1.getAction() == MotionEvent.ACTION_MOVE) {
				// return true;
				// }
				// return false;
				// }
				// });
			} else {
				gifWebview.setVisibility(View.GONE);
				imageView.setVisibility(View.VISIBLE);
				imageCacheloader.displayImage(mediaObject.filePath,
						R.drawable.image_placeholder, imageView);
				if (!isFragment) {
					imageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(context,
									ImageZoomActivity.class);
							intent.putExtra("image", mediaObject.filePath);
							context.startActivity(intent);
						}
					});
				}

			}

			frameVideoView.setVisibility(View.GONE);
			frameImageView.setVisibility(View.VISIBLE);
			frameYoutubeView.setVisibility(View.GONE);

		} else if (mediaObject.fileType.equalsIgnoreCase("video")) {
			frameVideoView.setVisibility(View.VISIBLE);
			frameImageView.setVisibility(View.GONE);
			frameYoutubeView.setVisibility(View.GONE);

			MediaController mediaController = new MediaController(context);
			mediaController.setAnchorView(videoView);
			videoView.setMediaController(mediaController);
			videoView.setVideoPath(mediaObject.filePath);
			videoView.requestFocus();
			videoView.seekTo(0);
			// videoView.start();

			videoView.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					// TODO Auto-generated method stub
					System.err.println("error while playng");
					return false;
				}
			});
			videoView.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					System.err.println("error while playng");
				}
			});
			videoView.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					videoView.start();
				}
			});
		} else if (mediaObject.fileType.equalsIgnoreCase("youtube")) {

			if (isFragment) {
				// View youtubeView = inflater.inflate(
				// R.layout.youtube_player_fragment_layout, null, false);
				// frameYoutubeView.addView(youtubeView);
				// youtubeplayerFragment = (YouTubePlayerSupportFragment)
				// youtubeView
				// .findViewById(R.id.youtubeplayerFragment);

				// youtubeplayerFragment.initialize(
				// getResources().getString(R.string.map_key), this);

				// YouTubePlayerSupportFragment youTubePlayerFragment =
				// YouTubePlayerSupportFragment
				// .newInstance();
				// FragmentTransaction transaction = getChildFragmentManager()
				// .beginTransaction();
				// transaction.add(R.id.frameYoutubeView, youTubePlayerFragment)
				// .commit();
				// gifWebview.setVisibility(View.VISIBLE);
				// imageView.setVisibility(View.GONE);

				frameImageView.setVisibility(View.GONE);
				frameYoutubeView.setVisibility(View.GONE);
				frameVideoView.setVisibility(View.VISIBLE);

				gifWebview.setVisibility(View.GONE);
				imageView.setVisibility(View.GONE);

				videoView.setOnErrorListener(new OnErrorListener() {

					@Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
						// TODO Auto-generated method stub
						System.err.println("error while playng");
						// videoView.stopPlayback();
						// videoView.start();
						return false;
					}
				});
				videoView.setOnPreparedListener(new OnPreparedListener() {

					@Override
					public void onPrepared(MediaPlayer arg0) {
						// TODO Auto-generated method stub
						// System.err.println("error while playng");
						videoView.seekTo(0);
					}
				});
				videoView.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer arg0) {
						// TODO Auto-generated method stub
						videoView.start();
					}
				});
				MediaController mediaController = new MediaController(context);
				mediaController.setAnchorView(videoView);
				videoView.setMediaController(mediaController);

				// loadYouTubeVideo();
				RTSPUrlTask truitonTask = new RTSPUrlTask();
				truitonTask.execute(mediaObject.filePath);

			} else {
				frameVideoView.setVisibility(View.GONE);
				frameImageView.setVisibility(View.GONE);
				frameYoutubeView.setVisibility(View.VISIBLE);
				View youtubeView = inflater.inflate(
						R.layout.youtube_player_view_layout, null, false);
				youtubeplayerview = (YouTubePlayerView) youtubeView
						.findViewById(R.id.youtubeplayerview);
				frameYoutubeView.addView(youtubeView);
				youtubeplayerview.initialize(
						getResources().getString(R.string.map_key), this);
			}

		}
	}

	void loadGifImage() {

		if (Utils.hasNetworkConnection(context.getApplicationContext())) {

			String htnlString = "<!DOCTYPE html><html><body style=\"background-color:transparent;margin: 0; padding: 0\"><img src=\""
					+ mediaObject.filePath
					+ "\" alt=\"pageNo\" width=\"100%\" height=\"100%\"></body></html>";
			gifWebview.loadDataWithBaseURL(null, htnlString, "text/html",
					"UTF-8", null);
			gifWebview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
		} else {
			gifWebview.setVisibility(View.GONE);
			imageView.setVisibility(View.VISIBLE);
		}
	}

	void loadYouTubeVideo() {

		if (Utils.hasNetworkConnection(context.getApplicationContext())) {

			gifWebview.getSettings().setJavaScriptEnabled(true);
			gifWebview.getSettings().setPluginState(WebSettings.PluginState.ON);
			// gifWebview.getSettings().setUseWideViewPort(true);
			gifWebview
					.getSettings()
					.setUserAgentString(
							"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/534.36 (KHTML, like Gecko) Chrome/13.0.766.0 Safari/534.36");
			gifWebview.setWebChromeClient(new WebChromeClient() {
			});
			gifWebview.setWebViewClient(new WebViewClient());

			String id = getYTVideoId(mediaObject.filePath);
			String html = getHTML(id);
			gifWebview.loadDataWithBaseURL(null, html, "text/html", "UTF-8",
					null);
			// gifWebview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
		} else {
			gifWebview.setVisibility(View.GONE);
			imageView.setVisibility(View.VISIBLE);
		}
	}

	public String getHTML(String videoId) {

		String html = "<!DOCTYPE html><html><body style=\"background-color:transparent;margin: 0; padding: 0\"><iframe class=\"youtube-player\" "
				+ "style=\"border: 0; width: 100%; height: 100%;"
				+ "padding:0px; margin:0px\" "
				+ "id=\"ytplayer\" type=\"text/html\" "
				+ "src=\"http://www.youtube.com/embed/"
				+ videoId
				+ "?fs=0\" frameborder=\"0\" "
				+ "allowfullscreen autobuffer "
				+ "controls onclick=\"this.play()\">\n"
				+ "</iframe>\n</body></html>";

		return html;
	}

	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub
		Log.e("error", "");
	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer player,
			boolean arg2) {
		// TODO Auto-generated method stub

		String id = getYTVideoId(mediaObject.filePath);
		player.cueVideo(id);
		youtubePlayer = player;
		// o7VVHhK9zf0

	}

	String getYTVideoId(String url) {
		String[] compo = url.split("v=");
		int count = compo.length;
		String id = compo[count - 1];
		return id;
	}

	void pauseVideo() {
		if (mediaObject.fileType.equalsIgnoreCase("video")) {

			videoView.pause();
		} else if (mediaObject.fileType.equalsIgnoreCase("youtube")) {

			if (youtubePlayer != null) {
				youtubePlayer.pause();
			}
			if (gifWebview != null) {
				gifWebview.onPause();
			}
			if (isFragment) {
				videoView.pause();
			}
		}
	}

	void playVideo() {
		if (mediaObject.fileType.equalsIgnoreCase("video")) {
			videoView.requestFocus();
			videoView.start();
		} else if (mediaObject.fileType.equalsIgnoreCase("youtube")) {

			if (youtubePlayer != null) {
				youtubePlayer.play();
			}
			if (gifWebview != null) {
				gifWebview.onResume();
				// loadYouTubeVideo();
				// gifWebview.reload();
			}
			if (isFragment) {
				videoView.requestFocus();
				videoView.start();
			}
		}
	}

	private class RTSPUrlTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			String response = getRTSPVideoUrl(urls[0]);
			return response;
		}

		void startPlaying(String url) {
			Uri uriYouTube = Uri.parse(url);
			videoView.setVideoURI(uriYouTube);
			videoView.requestFocus();
			// videoView.start();
			videoView.seekTo(0);
		}

		@Override
		protected void onPostExecute(String result) {
			startPlaying(result);
		}

		public String getRTSPVideoUrl(String urlYoutube) {
			try {
				String gdy = "http://gdata.youtube.com/feeds/api/videos/";
				DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				String id = extractYoutubeId(urlYoutube);
				URL url = new URL(gdy + id);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				org.w3c.dom.Document doc = dBuilder.parse(connection
						.getInputStream());
				Element el = doc.getDocumentElement();
				NodeList list = el.getElementsByTagName("media:content");
				String cursor = urlYoutube;
				for (int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					if (node != null) {
						NamedNodeMap nodeMap = node.getAttributes();
						HashMap<String, String> maps = new HashMap<String, String>();
						for (int j = 0; j < nodeMap.getLength(); j++) {
							Attr att = (Attr) nodeMap.item(j);
							maps.put(att.getName(), att.getValue());
						}
						if (maps.containsKey("yt:format")) {
							String f = maps.get("yt:format");
							if (maps.containsKey("url"))
								cursor = maps.get("url");
							if (f.equals("1"))
								return cursor;
						}
					}
				}
				return cursor;
			} catch (Exception ex) {
				return urlYoutube;
			}
		}
	}

	public String extractYoutubeId(String url) throws MalformedURLException {
		String query = new URL(url).getQuery();
		String[] param = query.split("&");
		String id = null;
		for (String row : param) {
			String[] param1 = row.split("=");
			if (param1[0].equals("v")) {
				id = param1[1];
			}
		}
		return id;
	}

}
