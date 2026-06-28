package com.metafold.videodownloader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.webkit.CookieManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;
import com.yausername.youtubedl_android.YoutubeDLRequest;
import com.yausername.youtubedl_android.YoutubeDLResponse;
import com.yausername.youtubedl_android.mapper.VideoFormat;
import com.yausername.youtubedl_android.mapper.VideoInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.Unit;
import kotlin.jvm.functions.Function3;

public final class MainActivity extends Activity {
    private static final String PROCESS_ID = "metafold-video-download";
    private static final String PLAYLIST_LIST_PROCESS_ID = PROCESS_ID + "-playlist-list";
    private static final String PLAYLIST_ENTRY_PREFIX = "METAFOLD_ENTRY\t";
    private static final String DOWNLOAD_FOLDER = "MetaFold Downloader";
    private static final String PREFS_NAME = "downloader";
    private static final String PREF_LAST_UPDATE = "last_ytdlp_update";
    private static final String PREF_LANGUAGE = "language";
    private static final String PREF_DEFAULT_RESOLUTION = "default_resolution";
    private static final String PREF_POPUP_RESOLUTION = "popup_resolution";
    private static final String PREF_LIMIT_MOBILE_RESOLUTION = "limit_mobile_resolution";
    private static final String PREF_SHOW_HIGH_RESOLUTION = "show_high_resolution";
    private static final String PREF_VIDEO_FORMAT = "video_format";
    private static final String PREF_AUDIO_FORMAT = "audio_format";
    private static final String PREF_ORIGINAL_AUDIO = "original_audio";
    private static final String PREF_DESCRIPTIVE_AUDIO = "descriptive_audio";
    private static final String PREF_ASK_DOWNLOAD_LOCATION = "ask_download_location";
    private static final String PREF_USE_SAF = "use_saf";
    private static final String PREF_VIDEO_FOLDER_URI = "video_folder_uri";
    private static final String PREF_AUDIO_FOLDER_URI = "audio_folder_uri";
    private static final String PREF_FILENAME_CHARS = "filename_chars";
    private static final String PREF_REPLACEMENT_CHAR = "replacement_char";
    private static final String PREF_MAX_RETRIES = "max_retries";
    private static final String PREF_PAUSE_METERED = "pause_metered";
    private static final String PREF_LIMIT_QUEUE = "limit_queue";
    private static final String PREF_THEME = "theme";
    private static final String PREF_NIGHT_THEME = "night_theme";
    private static final String PREF_HIGH_REFRESH_RATE = "high_refresh_rate";
    private static final String PREF_SHOW_QUEUE_TIP = "show_queue_tip";
    private static final String PREF_TABLET_MODE = "tablet_mode";
    private static final String PREF_LIST_MODE = "list_mode";
    private static final String PREF_BOTTOM_TABS = "bottom_tabs";
    private static final String PREF_WATCH_HISTORY = "watch_history";
    private static final String PREF_RESUME_PLAYBACK = "resume_playback";
    private static final String PREF_PLAYLIST_POSITIONS = "playlist_positions";
    private static final String PREF_SEARCH_HISTORY = "search_history";
    private static final String PREF_CONTENT_LANGUAGE = "content_language";
    private static final String PREF_CONTENT_COUNTRY = "content_country";
    private static final String PREF_HOME_CONTENT = "home_content";
    private static final String PREF_CHANNEL_TABS = "channel_tabs";
    private static final String PREF_SHOW_AGE_RESTRICTED = "show_age_restricted";
    private static final String PREF_YOUTUBE_RESTRICTED_MODE = "youtube_restricted_mode";
    private static final String PREF_SEARCH_SUGGESTIONS = "search_suggestions";
    private static final String PREF_IMAGE_QUALITY = "image_quality";
    private static final String PREF_SHOW_COMMENTS = "show_comments";
    private static final String PREF_SHOW_NEXT_RELATED = "show_next_related";
    private static final String PREF_SHOW_DESCRIPTION = "show_description";
    private static final String PREF_SHOW_INFO_BOX = "show_info_box";
    private static final String PREF_FEED_THRESHOLD = "feed_threshold";
    private static final String PREF_DEDICATED_FEED = "dedicated_feed";
    private static final String PREF_UPDATE_NOTIFICATIONS = "update_notifications";
    private static final String PREF_APP_UPDATE_LAST_CHECK = "app_update_last_check";
    private static final String PREF_APP_UPDATE_REMIND_VERSION = "app_update_remind_version";
    private static final String PREF_APP_UPDATE_REMIND_AFTER = "app_update_remind_after";
    private static final String PREF_MANDATORY_UPDATE_VERSION = "mandatory_update_version";
    private static final String PREF_MANDATORY_UPDATE_HTML_URL = "mandatory_update_html_url";
    private static final String PREF_MANDATORY_UPDATE_APK_URL = "mandatory_update_apk_url";
    private static final String PREF_MANDATORY_UPDATE_RELEASE_NAME = "mandatory_update_release_name";
    private static final String PREF_HIDDEN_DOWNLOAD_KEYS = "hidden_download_keys";
    private static final String PREF_LICENSE_KEY = "license_key";
    private static final String PREF_LICENSE_EMAIL = "license_email";
    private static final String PREF_LICENSE_REQUEST_ID = "license_request_id";
    private static final String PREF_LICENSE_STATUS = "license_status";
    private static final String PREF_LICENSE_OWNER = "license_owner";
    private static final String PREF_LICENSE_EXPIRES_AT = "license_expires_at";
    private static final String PREF_LICENSE_LAST_CHECK = "license_last_check";
    private static final String PREF_LICENSE_BOUND_DEVICE_ID = "license_bound_device_id";
    private static final String PREF_LICENSE_BOUND_DEVICE_LABEL = "license_bound_device_label";
    private static final String PREF_LICENSE_NEXT_DEVICE_CHANGE = "license_next_device_change";
    private static final String PREF_AUTH_EMAIL = "auth_email";
    private static final String PREF_AUTH_ID_TOKEN = "auth_id_token";
    private static final String PREF_AUTH_REFRESH_TOKEN = "auth_refresh_token";
    private static final String PREF_AUTH_LOCAL_ID = "auth_local_id";
    private static final String PREF_AUTH_TOKEN_EXPIRES_AT = "auth_token_expires_at";
    private static final String PREF_PLAYER_NOTIFICATION = "player_notification";
    private static final String PREF_NEW_FEED_NOTIFICATIONS = "new_feed_notifications";
    private static final String PREF_NOTIFICATION_CHECK_FREQUENCY = "notification_check_frequency";
    private static final String PREF_NOTIFICATION_NETWORK = "notification_network";
    private static final String PREF_NOTIFICATION_CHANNELS = "notification_channels";
    private static final String JOB_METADATA_FILE = "metafold-download.properties";
    private static final int REQUEST_VIDEO_FOLDER = 4101;
    private static final int REQUEST_AUDIO_FOLDER = 4102;
    private static final String DONATION_RECIPIENT = "Ahmet Doğan";
    private static final String DONATION_IBAN = "";
    private static final String DONATION_NOTE = "MetaFold Downloader kahve desteği";
    private static final String DONATION_PAYMENT_LINK = "";
    private static final String LICENSE_WHATSAPP_NUMBER = "905357309054";
    private static final String GITHUB_LATEST_RELEASE_API = "https://api.github.com/repos/metafold-dev/metafold-downloader-android/releases/latest";
    private static final String GITHUB_RELEASES_URL = "https://github.com/metafold-dev/metafold-downloader-android/releases/latest";
    private static final long UPDATE_REMIND_LATER_MS = 6L * 60L * 60L * 1000L;
    private static final boolean LICENSE_REQUIRED = true;
    private static final String FIREBASE_PROJECT_ID = "metafold-downloader";
    private static final String FIREBASE_WEB_API_KEY = "AIzaSyDp9eEdjo-pPS76MVTR8O-cmlWtYycXSy0";
    private static final String FIREBASE_AUTH_BASE_URL = "https://identitytoolkit.googleapis.com/v1/";
    private static final String FIREBASE_TOKEN_REFRESH_URL = "https://securetoken.googleapis.com/v1/token";
    private static final String FIRESTORE_LICENSE_COLLECTION = "license_requests";
    private static final String LICENSE_STATUS_ACTIVE = "active";
    private static final String LICENSE_STATUS_PENDING = "pending";
    private static final String LICENSE_STATUS_INACTIVE = "inactive";
    private static final String LICENSE_STATUS_DEVICE_LOCKED = "device_locked";
    private static final long DEVICE_CHANGE_COOLDOWN_MS = 7L * 24L * 60L * 60L * 1000L;
    private static final long LICENSE_REFRESH_INTERVAL_MS = 15L * 60L * 1000L;
    private static final long BACK_EXIT_WINDOW_MS = 1800L;
    private static final long UPDATE_INTERVAL_MS = 12L * 60L * 60L * 1000L;
    private static final Pattern URL_PATTERN = Pattern.compile(
            "((?:https?://|//)?(?:[a-z0-9-]+\\.)*(?:youtu\\.be|youtube\\.com|youtube-nocookie\\.com|instagram\\.com|facebook\\.com|fb\\.watch|tiktok\\.com|vm\\.tiktok\\.com|vt\\.tiktok\\.com|pinterest\\.com|pin\\.it|x\\.com|twitter\\.com)\\S*)",
            Pattern.CASE_INSENSITIVE
    );
    private static final SocialPlatform[] PLATFORMS = new SocialPlatform[]{
            new SocialPlatform("YouTube", "https://www.youtube.com/", R.drawable.ic_youtube, Color.rgb(220, 38, 38)),
            new SocialPlatform("Facebook", "https://www.facebook.com/", R.drawable.ic_facebook, Color.rgb(24, 119, 242)),
            new SocialPlatform("Instagram", "https://www.instagram.com/", R.drawable.ic_instagram, Color.rgb(225, 48, 108)),
            new SocialPlatform("TikTok", "https://www.tiktok.com/", R.drawable.ic_tiktok, Color.rgb(0, 150, 136)),
            new SocialPlatform("Pinterest", "https://www.pinterest.com/", R.drawable.ic_pinterest, Color.rgb(230, 0, 35)),
            new SocialPlatform("X / Twitter", "https://x.com/", R.drawable.ic_x, Color.rgb(23, 32, 29))
    };
    private static final AppThemeOption[] APP_THEMES = new AppThemeOption[]{
            new AppThemeOption("light", "Açık", "Light", "Temiz açık arayüz", "Clean light interface",
                    Color.rgb(8, 127, 111), Color.rgb(244, 247, 244), Color.WHITE, Color.rgb(248, 250, 248),
                    Color.rgb(222, 229, 225), Color.rgb(23, 32, 29), Color.rgb(96, 112, 106), Color.WHITE),
            new AppThemeOption("dark", "Koyu", "Dark", "Gerçek koyu arayüz", "True dark interface",
                    Color.rgb(76, 166, 150), Color.rgb(12, 16, 18), Color.rgb(24, 30, 33), Color.rgb(31, 39, 42),
                    Color.rgb(54, 66, 70), Color.rgb(232, 239, 236), Color.rgb(158, 174, 168), Color.rgb(8, 14, 16))
    };

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ExecutorService licenseExecutor = Executors.newSingleThreadExecutor();
    private final Object initLock = new Object();
    private final List<Button> optionButtons = new ArrayList<>();
    private final Deque<QueuedDownload> downloadQueue = new ArrayDeque<>();
    private final List<File> displayedDownloads = new ArrayList<>();
    private final Set<String> selectedDownloadPaths = new HashSet<>();
    private final Map<String, PlaylistStatusRow> playlistStatusRows = new LinkedHashMap<>();
    private final Set<Integer> skippedPlaylistIndexes = new HashSet<>();
    private final Map<Integer, File> playlistDownloadedFiles = new LinkedHashMap<>();

    private LinearLayout authPanel;
    private LinearLayout homePanel;
    private LinearLayout downloadPanel;
    private LinearLayout browserPanel;
    private LinearLayout downloadsPanel;
    private LinearLayout settingsPanel;
    private LinearLayout aboutPanel;
    private FrameLayout drawerOverlay;
    private LinearLayout drawerPanel;
    private LinearLayout activePlatformCard;
    private LinearLayout downloadsList;
    private LinearLayout downloadsSelectionBar;
    private LinearLayout playlistModeRow;
    private LinearLayout playlistStatusPanel;
    private LinearLayout playlistStatusList;
    private LinearLayout optionList;
    private LinearLayout lastDownloadSummary;
    private View appHeaderView;
    private EditText urlInput;
    private EditText downloadSearchInput;
    private Button analyzeButton;
    private Button downloadButton;
    private Button cancelButton;
    private Button pasteButton;
    private Button openButton;
    private Button shareButton;
    private Button updateButton;
    private Button selectAllDownloadsButton;
    private Button deleteSelectedDownloadsButton;
    private Button browserDownloadButton;
    private Switch playlistSwitch;
    private ProgressBar progressBar;
    private TextView statusView;
    private TextView platformView;
    private TextView outputView;
    private TextView optionHeaderView;
    private TextView playlistStatusTitle;
    private TextView browserUrlView;
    private TextView downloadSelectionStatus;
    private ImageView activePlatformIcon;
    private TextView activePlatformNameView;
    private TextView activePlatformUrlView;
    private WebView webView;
    private AlertDialog mandatoryUpdateDialog;
    private Dialog updateDownloadDialog;
    private AnimatedUpdateProgressView updateDownloadProgress;
    private TextView updateDownloadStatus;
    private TextView updateDownloadPercent;
    private TextView updateDownloadBytes;

    private boolean downloaderReady;
    private boolean ffmpegReady;
    private boolean downloading;
    private boolean busy;
    private boolean browserPageDownloadAvailable;
    private boolean pendingAutoFetch;
    private boolean downloadSelectionMode;
    private boolean currentInfoPlaylistMode;
    private boolean activePlaylistDownload;
    private boolean licenseRefreshInProgress;
    private boolean mandatoryUpdateRequired;
    private volatile boolean cancelRequested;
    private volatile boolean skipCurrentRequested;
    private int activePlaylistIndex;
    private int activePlaylistTotal;
    private int completedPlaylistCount;
    private int failedPlaylistCount;
    private String currentInfoUrl = "";
    private String activePlaylistKey = "";
    private String selectedLanguage = "tr";
    private SocialPlatform activePlatform = PLATFORMS[0];
    private DownloadOption selectedOption;
    private File lastFile;
    private File pendingUpdateApk;
    private Uri lastUri;
    private String lastMimeType = "video/mp4";
    private long lastBackPressedAt;
    private boolean progressPulseRunning;

    private final Function3<Float, Long, String, Unit> progressCallback = (progress, etaInSeconds, line) -> {
        mainHandler.post(() -> {
            if (progress != null && progress >= 0f) {
                int percent = Math.min(100, Math.max(0, Math.round(progress)));
                if (activePlaylistDownload) {
                    updateActivePlaylistProgress(percent);
                    if (activePlaylistTotal > 0) {
                        int overall = Math.min(100, Math.max(0, Math.round(((completedPlaylistCount + (percent / 100f)) / activePlaylistTotal) * 100f)));
                        progressBar.setIndeterminate(false);
                        progressBar.setProgress(overall);
                        setStatus("Playlist indiriliyor: " + completedPlaylistCount + "/" + activePlaylistTotal + " - video %" + percent, true);
                        return;
                    }
                    if (percent >= 100) {
                        progressBar.setIndeterminate(true);
                        setStatus("Sıradaki video başlatılıyor...", true);
                        return;
                    }
                }
                progressBar.setIndeterminate(false);
                progressBar.setProgress(percent);
                setStatus(downloadProgressText(percent, etaInSeconds), true);
            }
        });
        return Unit.INSTANCE;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enterFullscreen();
        selectedLanguage = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString(PREF_LANGUAGE, "tr");
        setContentView(createContentView());
        restoreMandatoryUpdateLock();
        applyRefreshRatePreference();
        setupWebView();
        pendingAutoFetch = handleIncomingIntent(getIntent());
        if (!isSignedIn()) {
            pendingAutoFetch = false;
            showAuthPanel(false);
        } else {
            refreshLicenseSilently(false);
        }
        setBusy(true, "İndirme altyapısı kontrol ediliyor...");
        executor.execute(() -> {
            try {
                ensureDownloaderReady();
                UpdateResult updateResult = updateYoutubeDl(false);
                mainHandler.post(() -> {
                    setBusy(false, null);
                    setStatus(updateResult.message, true);
                    if (pendingAutoFetch) {
                        pendingAutoFetch = false;
                        fetchFormatOptions(false);
                    } else {
                        maybeCheckAppUpdateOnStartup();
                    }
                });
            } catch (Exception error) {
                mainHandler.post(() -> {
                    setBusy(false, null);
                    setStatus("Başlatma hatası.", false);
                    outputView.setText(cleanError(error));
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pendingUpdateApk != null && pendingUpdateApk.exists() && canRequestPackageInstalls()) {
            File apk = pendingUpdateApk;
            pendingUpdateApk = null;
            launchPackageInstaller(apk);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (mandatoryUpdateRequired) {
            showMandatoryUpdateDialog(storedMandatoryUpdateInfo());
            return;
        }
        boolean shouldFetch = handleIncomingIntent(intent);
        if (shouldFetch && !isSignedIn()) {
            pendingAutoFetch = false;
            showAuthPanel(true);
            return;
        }
        if (shouldFetch && downloaderReady && !busy && !downloading) {
            fetchFormatOptions(false);
        } else if (shouldFetch) {
            pendingAutoFetch = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_VIDEO_FOLDER || requestCode == REQUEST_AUDIO_FOLDER)
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            Uri uri = data.getData();
            int flags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            try {
                getContentResolver().takePersistableUriPermission(uri, flags);
            } catch (Exception ignored) {
                // Some providers grant access without persistable permissions.
            }
            putString(requestCode == REQUEST_VIDEO_FOLDER ? PREF_VIDEO_FOLDER_URI : PREF_AUDIO_FOLDER_URI, uri.toString());
            toast("İndirme klasörü seçildi");
            if (settingsPanel != null && settingsPanel.getVisibility() == View.VISIBLE) {
                renderDownloadSettings(settingsPanel);
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            enterFullscreen();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerOverlay != null && drawerOverlay.getVisibility() == View.VISIBLE) {
            closeDrawer();
            return;
        }
        if (mandatoryUpdateRequired) {
            showMandatoryUpdateDialog(storedMandatoryUpdateInfo());
            return;
        }
        if (browserPanel != null && browserPanel.getVisibility() == View.VISIBLE && webView != null && webView.canGoBack()) {
            webView.goBack();
            return;
        }
        if (settingsPanel != null && settingsPanel.getVisibility() == View.VISIBLE && isSettingsSubPage()) {
            renderSettingsHome(settingsPanel);
            return;
        }
        if ((browserPanel != null && browserPanel.getVisibility() == View.VISIBLE)
                || (downloadsPanel != null && downloadsPanel.getVisibility() == View.VISIBLE)
                || (settingsPanel != null && settingsPanel.getVisibility() == View.VISIBLE)
                || (aboutPanel != null && aboutPanel.getVisibility() == View.VISIBLE)
                || (downloadPanel != null && downloadPanel.getVisibility() == View.VISIBLE)) {
            showHome();
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastBackPressedAt > BACK_EXIT_WINDOW_MS) {
            lastBackPressedAt = now;
            toast("Çıkmak için tekrar geri tuşuna basın");
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        try {
            YoutubeDL.getInstance().destroyProcessById(PROCESS_ID);
        } catch (Exception ignored) {
            // Process may already be stopped.
        }
        executor.shutdownNow();
        licenseExecutor.shutdownNow();
        super.onDestroy();
    }

    private void enterFullscreen() {
        Window window = getWindow();
        AppThemeOption theme = currentTheme();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(theme.backgroundColor);
            window.setNavigationBarColor(theme.backgroundColor);
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
        applyRefreshRatePreference();
    }

    private void applyRefreshRatePreference() {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        if (!getBool(PREF_HIGH_REFRESH_RATE, true)) {
            params.preferredRefreshRate = 0f;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                params.preferredDisplayModeId = 0;
            }
            window.setAttributes(params);
            applyViewFrameRate(window.getDecorView(), 0f);
            return;
        }

        RefreshMode refreshMode = bestRefreshMode();
        if (refreshMode.rate <= 0f) {
            return;
        }

        params.preferredRefreshRate = refreshMode.rate;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && refreshMode.modeId > 0) {
            params.preferredDisplayModeId = refreshMode.modeId;
        }
        window.setAttributes(params);
        applyViewFrameRate(window.getDecorView(), refreshMode.rate);
    }

    private RefreshMode bestRefreshMode() {
        Display display = activityDisplay();
        if (display == null) {
            return new RefreshMode(0f, 0);
        }

        float bestRate = display.getRefreshRate();
        int bestModeId = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Display.Mode currentMode = display.getMode();
            Display.Mode bestMode = null;
            Display.Mode fallbackMode = null;
            Display.Mode[] modes = display.getSupportedModes();
            if (modes != null) {
                for (Display.Mode mode : modes) {
                    if (mode == null) {
                        continue;
                    }
                    if (fallbackMode == null || mode.getRefreshRate() > fallbackMode.getRefreshRate()) {
                        fallbackMode = mode;
                    }
                    if (currentMode != null
                            && mode.getPhysicalWidth() == currentMode.getPhysicalWidth()
                            && mode.getPhysicalHeight() == currentMode.getPhysicalHeight()
                            && (bestMode == null || mode.getRefreshRate() > bestMode.getRefreshRate())) {
                        bestMode = mode;
                    }
                }
            }
            if (bestMode == null) {
                bestMode = fallbackMode;
            }
            if (bestMode != null) {
                bestRate = bestMode.getRefreshRate();
                bestModeId = bestMode.getModeId();
            }
        }
        return new RefreshMode(bestRate, bestModeId);
    }

    private Display activityDisplay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return getDisplay();
        }
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        return manager == null ? null : manager.getDefaultDisplay();
    }

    private void applyViewFrameRate(View view, float frameRate) {
        if (view == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            return;
        }
        try {
            Method method = View.class.getMethod("setFrameRate", float.class, int.class);
            method.invoke(view, frameRate, 0);
        } catch (Exception ignored) {
            // Some vendor builds ignore this hint; the window refresh preference still applies.
        }
    }

    private View createContentView() {
        AppThemeOption theme = currentTheme();
        int bg = theme.backgroundColor;
        int text = theme.textColor;
        int muted = theme.mutedColor;
        int accent = theme.accentColor;

        FrameLayout appFrame = new FrameLayout(this);
        appFrame.setBackgroundColor(bg);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(bg);
        appFrame.addView(scrollView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        int sidePadding = dp(18);
        int topPadding = dp(18);
        int bottomPadding = dp(24);
        root.setPadding(sidePadding, topPadding + topSafeInsetFallback(), sidePadding, bottomPadding);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            root.setOnApplyWindowInsetsListener((view, insets) -> {
                int safeTop = insets.getSystemWindowInsetTop();
                int safeLeft = insets.getSystemWindowInsetLeft();
                int safeRight = insets.getSystemWindowInsetRight();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && insets.getDisplayCutout() != null) {
                    safeTop = Math.max(safeTop, insets.getDisplayCutout().getSafeInsetTop());
                    safeLeft = Math.max(safeLeft, insets.getDisplayCutout().getSafeInsetLeft());
                    safeRight = Math.max(safeRight, insets.getDisplayCutout().getSafeInsetRight());
                }
                view.setPadding(sidePadding + safeLeft, topPadding + safeTop, sidePadding + safeRight, bottomPadding);
                return insets;
            });
            root.requestApplyInsets();
        }
        scrollView.addView(root, new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.WRAP_CONTENT
        ));

        appHeaderView = createAppHeader();
        root.addView(appHeaderView, matchWrap());

        authPanel = createAuthPanel();
        LinearLayout.LayoutParams authParams = matchWrap();
        authParams.topMargin = dp(12);
        root.addView(authPanel, authParams);

        homePanel = createHomePanel();
        LinearLayout.LayoutParams homeParams = matchWrap();
        homeParams.topMargin = dp(12);
        root.addView(homePanel, homeParams);

        downloadPanel = new LinearLayout(this);
        downloadPanel.setOrientation(LinearLayout.VERTICAL);
        downloadPanel.setPadding(dp(14), dp(14), dp(14), dp(14));
        downloadPanel.setBackground(rounded(theme.surfaceColor, 8, theme.borderColor));
        LinearLayout.LayoutParams panelParams = matchWrap();
        panelParams.topMargin = dp(12);
        root.addView(downloadPanel, panelParams);

        platformView = textView("Link modu", 13, muted, true);
        downloadPanel.addView(platformView);

        urlInput = new EditText(this);
        urlInput.setHint(ui("Video linki"));
        urlInput.setSingleLine(false);
        urlInput.setMinLines(2);
        urlInput.setMaxLines(4);
        urlInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        urlInput.setTextColor(text);
        urlInput.setHintTextColor(muted);
        urlInput.setTextSize(15);
        urlInput.setPadding(dp(12), dp(10), dp(12), dp(10));
        urlInput.setBackground(rounded(theme.surfaceAltColor, 8, theme.borderColor));
        LinearLayout.LayoutParams inputParams = matchWrap();
        inputParams.topMargin = dp(10);
        downloadPanel.addView(urlInput, inputParams);

        LinearLayout quickRow = new LinearLayout(this);
        quickRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams quickParams = matchWrap();
        quickParams.topMargin = dp(10);
        downloadPanel.addView(quickRow, quickParams);

        pasteButton = secondaryButton("Yapıştır");
        pasteButton.setOnClickListener(v -> pasteFromClipboard());
        quickRow.addView(pasteButton, rowWeight());

        Button clearButton = secondaryButton("Temizle");
        clearButton.setOnClickListener(v -> clearSelection());
        LinearLayout.LayoutParams clearParams = rowWeight();
        clearParams.leftMargin = dp(8);
        quickRow.addView(clearButton, clearParams);

        analyzeButton = secondaryButton("İndirme seçeneklerini getir");
        analyzeButton.setOnClickListener(v -> fetchFormatOptions(false));
        LinearLayout.LayoutParams analyzeParams = matchWrap();
        analyzeParams.topMargin = dp(10);
        downloadPanel.addView(analyzeButton, analyzeParams);

        playlistModeRow = new LinearLayout(this);
        playlistModeRow.setOrientation(LinearLayout.HORIZONTAL);
        playlistModeRow.setGravity(Gravity.CENTER_VERTICAL);
        playlistModeRow.setPadding(dp(10), dp(10), dp(10), dp(10));
        playlistModeRow.setBackground(rounded(theme.surfaceAltColor, 8, theme.borderColor));
        playlistModeRow.setVisibility(View.GONE);
        LinearLayout playlistTexts = new LinearLayout(this);
        playlistTexts.setOrientation(LinearLayout.VERTICAL);
        playlistModeRow.addView(playlistTexts, rowTextParams());
        TextView playlistTitle = textView("Tüm playlist'i indir", 15, text, true);
        playlistTexts.addView(playlistTitle);
        TextView playlistHint = textView("Listedeki tüm videoları seçilen formatla indir", 12, muted, false);
        playlistHint.setPadding(0, dp(2), 0, 0);
        playlistTexts.addView(playlistHint);
        playlistSwitch = new Switch(this);
        playlistSwitch.setChecked(false);
        playlistSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> resetInfoForPlaylistToggle());
        playlistModeRow.setOnClickListener(v -> playlistSwitch.setChecked(!playlistSwitch.isChecked()));
        playlistModeRow.addView(playlistSwitch, new LinearLayout.LayoutParams(dp(74), LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams playlistParams = matchWrap();
        playlistParams.topMargin = dp(10);
        downloadPanel.addView(playlistModeRow, playlistParams);

        optionHeaderView = textView("Kalite seçenekleri henüz alınmadı.", 13, muted, false);
        optionHeaderView.setPadding(0, dp(12), 0, 0);
        downloadPanel.addView(optionHeaderView);

        optionList = new LinearLayout(this);
        optionList.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams optionParams = matchWrap();
        optionParams.topMargin = dp(8);
        downloadPanel.addView(optionList, optionParams);

        downloadButton = primaryButton("İndir", accent);
        downloadButton.setOnClickListener(v -> startDownload());
        LinearLayout.LayoutParams downloadParams = matchWrap();
        downloadParams.topMargin = dp(12);
        downloadPanel.addView(downloadButton, downloadParams);

        cancelButton = primaryButton("İptal", Color.rgb(183, 89, 20));
        cancelButton.setEnabled(false);
        cancelButton.setOnClickListener(v -> cancelDownload());
        LinearLayout.LayoutParams cancelParams = matchWrap();
        cancelParams.topMargin = dp(8);
        downloadPanel.addView(cancelButton, cancelParams);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        LinearLayout.LayoutParams progressParams = matchWrap();
        progressParams.topMargin = dp(14);
        downloadPanel.addView(progressBar, progressParams);

        statusView = textView("Başlatılıyor...", 14, muted, false);
        statusView.setPadding(0, dp(12), 0, 0);
        downloadPanel.addView(statusView);

        playlistStatusPanel = new LinearLayout(this);
        playlistStatusPanel.setOrientation(LinearLayout.VERTICAL);
        playlistStatusPanel.setVisibility(View.GONE);
        LinearLayout.LayoutParams playlistStatusParams = matchWrap();
        playlistStatusParams.topMargin = dp(10);
        downloadPanel.addView(playlistStatusPanel, playlistStatusParams);

        playlistStatusTitle = textView("Playlist durumu", 13, text, true);
        playlistStatusPanel.addView(playlistStatusTitle);

        playlistStatusList = new LinearLayout(this);
        playlistStatusList.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams playlistListParams = matchWrap();
        playlistListParams.topMargin = dp(6);
        playlistStatusPanel.addView(playlistStatusList, playlistListParams);

        outputView = textView("", 13, muted, false);
        outputView.setTextIsSelectable(true);
        outputView.setPadding(0, dp(10), 0, 0);
        downloadPanel.addView(outputView);

        lastDownloadSummary = new LinearLayout(this);
        lastDownloadSummary.setOrientation(LinearLayout.VERTICAL);
        lastDownloadSummary.setVisibility(View.GONE);
        LinearLayout.LayoutParams summaryParams = matchWrap();
        summaryParams.topMargin = dp(10);
        downloadPanel.addView(lastDownloadSummary, summaryParams);

        LinearLayout actionRow = new LinearLayout(this);
        actionRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams actionParams = matchWrap();
        actionParams.topMargin = dp(14);
        downloadPanel.addView(actionRow, actionParams);

        openButton = secondaryButton("Aç");
        openButton.setEnabled(false);
        openButton.setOnClickListener(v -> openLastFile());
        actionRow.addView(openButton, rowWeight());

        shareButton = secondaryButton("Paylaş");
        shareButton.setEnabled(false);
        shareButton.setOnClickListener(v -> shareLastFile());
        LinearLayout.LayoutParams shareParams = rowWeight();
        shareParams.leftMargin = dp(8);
        actionRow.addView(shareButton, shareParams);

        updateButton = secondaryButton("İndirme motorunu güncelle");
        updateButton.setOnClickListener(v -> updateExtractor());
        LinearLayout.LayoutParams updateParams = matchWrap();
        updateParams.topMargin = dp(12);
        downloadPanel.addView(updateButton, updateParams);

        browserPanel = createBrowserPanel();
        browserPanel.setVisibility(View.GONE);
        LinearLayout.LayoutParams browserParams = matchWrap();
        browserParams.topMargin = dp(12);
        root.addView(browserPanel, browserParams);

        downloadsPanel = createDownloadsPanel();
        downloadsPanel.setVisibility(View.GONE);
        LinearLayout.LayoutParams downloadsParams = matchWrap();
        downloadsParams.topMargin = dp(12);
        root.addView(downloadsPanel, downloadsParams);

        settingsPanel = createSettingsPanel();
        settingsPanel.setVisibility(View.GONE);
        LinearLayout.LayoutParams settingsParams = matchWrap();
        settingsParams.topMargin = dp(12);
        root.addView(settingsPanel, settingsParams);

        aboutPanel = createAboutPanel();
        aboutPanel.setVisibility(View.GONE);
        LinearLayout.LayoutParams aboutParams = matchWrap();
        aboutParams.topMargin = dp(12);
        root.addView(aboutPanel, aboutParams);

        TextView footer = textView("Oturumlar WebView içinde kalır. Yalnızca size ait veya indirme yetkiniz olan içerikleri indirin.", 12, muted, false);
        footer.setPadding(0, dp(14), 0, 0);
        root.addView(footer);

        if (isSignedIn()) {
            authPanel.setVisibility(View.GONE);
            downloadPanel.setVisibility(View.GONE);
            if (appHeaderView != null) {
                appHeaderView.setVisibility(View.VISIBLE);
            }
        } else {
            homePanel.setVisibility(View.GONE);
            downloadPanel.setVisibility(View.GONE);
            if (appHeaderView != null) {
                appHeaderView.setVisibility(View.GONE);
            }
        }

        drawerOverlay = createDrawerOverlay();
        drawerOverlay.setVisibility(View.GONE);
        appFrame.addView(drawerOverlay, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        forceDrawerClosed();

        return appFrame;
    }

    private LinearLayout createHomePanel() {
        AppThemeOption theme = currentTheme();
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);

        LinearLayout hero = new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setPadding(dp(16), dp(16), dp(16), dp(16));
        hero.setBackground(rounded(theme.surfaceColor, 8, theme.borderColor));
        panel.addView(hero, matchWrap());

        LinearLayout brandRow = new LinearLayout(this);
        brandRow.setOrientation(LinearLayout.HORIZONTAL);
        brandRow.setGravity(Gravity.CENTER_VERTICAL);
        hero.addView(brandRow, matchWrap());

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.app_icon);
        brandRow.addView(logo, new LinearLayout.LayoutParams(dp(58), dp(58)));

        LinearLayout titleBlock = new LinearLayout(this);
        titleBlock.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams titleBlockParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        titleBlockParams.leftMargin = dp(12);
        brandRow.addView(titleBlock, titleBlockParams);

        TextView title = textView("MetaFold Downloader", 24, theme.textColor, true);
        title.setSingleLine(true);
        title.setEllipsize(TextUtils.TruncateAt.END);
        titleBlock.addView(title);

        TextView subtitle = textView("Sosyal platformlar tek uygulamada", 13, theme.mutedColor, false);
        titleBlock.addView(subtitle);

        TextView intro = textView("Video bağlantılarını kalite seçerek indirin, platform akışlarına uygulama içinden geçin ve lisans/güncellemeleri tek yerden yönetin.", 14, theme.textColor, false);
        intro.setPadding(0, dp(14), 0, 0);
        hero.addView(intro);

        LinearLayout actionRow = new LinearLayout(this);
        actionRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams actionParams = matchWrap();
        actionParams.topMargin = dp(14);
        hero.addView(actionRow, actionParams);

        Button linkDownload = primaryButton("Link ile indir", theme.accentColor);
        linkDownload.setOnClickListener(v -> showDownloader());
        actionRow.addView(linkDownload, rowWeight());

        Button downloads = secondaryButton("İndirilenler");
        downloads.setOnClickListener(v -> showDownloads());
        LinearLayout.LayoutParams downloadsParams = rowWeight();
        downloadsParams.leftMargin = dp(8);
        actionRow.addView(downloads, downloadsParams);

        TextView platformTitle = textView("Platform kısayolları", 16, theme.textColor, true);
        LinearLayout.LayoutParams platformTitleParams = matchWrap();
        platformTitleParams.topMargin = dp(16);
        panel.addView(platformTitle, platformTitleParams);

        HorizontalScrollView platformScroll = new HorizontalScrollView(this);
        platformScroll.setHorizontalScrollBarEnabled(false);
        LinearLayout platformRow = new LinearLayout(this);
        platformRow.setOrientation(LinearLayout.HORIZONTAL);
        platformScroll.addView(platformRow, new HorizontalScrollView.LayoutParams(
                HorizontalScrollView.LayoutParams.WRAP_CONTENT,
                HorizontalScrollView.LayoutParams.WRAP_CONTENT
        ));
        for (SocialPlatform platform : PLATFORMS) {
            platformRow.addView(homePlatformShortcut(platform));
        }
        LinearLayout.LayoutParams platformParams = matchWrap();
        platformParams.topMargin = dp(8);
        panel.addView(platformScroll, platformParams);

        LinearLayout infoPanel = new LinearLayout(this);
        infoPanel.setOrientation(LinearLayout.VERTICAL);
        infoPanel.setPadding(dp(14), dp(14), dp(14), dp(14));
        infoPanel.setBackground(rounded(theme.surfaceColor, 8, theme.borderColor));
        LinearLayout.LayoutParams infoParams = matchWrap();
        infoParams.topMargin = dp(14);
        panel.addView(infoPanel, infoParams);

        infoPanel.addView(textView("Hızlı başlangıç", 16, theme.textColor, true));
        TextView info = textView("Bir platform akışını açabilir, başka uygulamadan paylaşılan linki yakalayabilir veya linki buraya yapıştırıp MP3/720p/1080p gibi seçeneklerle indirebilirsiniz.", 13, theme.mutedColor, false);
        info.setPadding(0, dp(6), 0, 0);
        infoPanel.addView(info);

        LinearLayout infoActions = new LinearLayout(this);
        infoActions.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams infoActionParams = matchWrap();
        infoActionParams.topMargin = dp(12);
        infoPanel.addView(infoActions, infoActionParams);

        Button settings = secondaryButton("Ayarlar");
        settings.setOnClickListener(v -> showSettings());
        infoActions.addView(settings, rowWeight());

        Button about = secondaryButton("Hakkında");
        about.setOnClickListener(v -> showAbout());
        LinearLayout.LayoutParams aboutParams = rowWeight();
        aboutParams.leftMargin = dp(8);
        infoActions.addView(about, aboutParams);

        return panel;
    }

    private View homePlatformShortcut(SocialPlatform platform) {
        AppThemeOption theme = currentTheme();
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);
        item.setPadding(dp(10), dp(10), dp(10), dp(10));
        item.setBackground(rounded(softAccent(platform.accentColor), 8, platform.accentColor));
        item.setOnClickListener(v -> openPlatform(platform));

        ImageView icon = new ImageView(this);
        icon.setImageResource(platform.iconRes);
        item.addView(icon, new LinearLayout.LayoutParams(dp(42), dp(42)));

        TextView name = textView(platform.name, 12, theme.textColor, true);
        name.setGravity(Gravity.CENTER);
        name.setSingleLine(true);
        name.setEllipsize(TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams nameParams = matchWrap();
        nameParams.topMargin = dp(6);
        item.addView(name, nameParams);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp(96), LinearLayout.LayoutParams.WRAP_CONTENT);
        params.rightMargin = dp(8);
        item.setLayoutParams(params);
        return item;
    }

    private LinearLayout createAuthPanel() {
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setGravity(Gravity.CENTER);
        panel.setPadding(0, dp(8), 0, dp(8));
        panel.setMinimumHeight(Math.max(dp(560), getResources().getDisplayMetrics().heightPixels - topSafeInsetFallback() - dp(150)));
        renderAuthPanel(panel, false);
        return panel;
    }

    private void renderAuthPanel(LinearLayout panel, boolean loginMode) {
        AppThemeOption theme = currentTheme();
        panel.removeAllViews();
        animateSettingsPanel(panel, loginMode);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(18), dp(18), dp(18), dp(18));
        card.setBackground(rounded(theme.surfaceColor, 8, theme.borderColor));
        LinearLayout.LayoutParams cardParams = matchWrap();
        cardParams.gravity = Gravity.CENTER;
        panel.addView(card, cardParams);

        LinearLayout banner = new LinearLayout(this);
        banner.setOrientation(LinearLayout.HORIZONTAL);
        banner.setGravity(Gravity.CENTER_VERTICAL);
        banner.setPadding(dp(12), dp(12), dp(12), dp(12));
        banner.setBackground(rounded(softAccent(theme.accentColor), 8, theme.accentColor));
        card.addView(banner, matchWrap());

        ImageView bannerLogo = new ImageView(this);
        bannerLogo.setImageResource(R.drawable.app_icon);
        banner.addView(bannerLogo, new LinearLayout.LayoutParams(dp(58), dp(58)));

        LinearLayout bannerTexts = new LinearLayout(this);
        bannerTexts.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams bannerTextParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        bannerTextParams.leftMargin = dp(12);
        banner.addView(bannerTexts, bannerTextParams);

        TextView brand = textView("MetaFold Downloader", 20, theme.textColor, true);
        brand.setSingleLine(true);
        brand.setEllipsize(TextUtils.TruncateAt.END);
        bannerTexts.addView(brand);

        TextView brandSubtitle = textView("Güvenli indirme hesabınız", 12, theme.mutedColor, false);
        brandSubtitle.setSingleLine(true);
        brandSubtitle.setEllipsize(TextUtils.TruncateAt.END);
        bannerTexts.addView(brandSubtitle);

        TextView title = textView(loginMode ? "Hesabınıza giriş yapın" : "Hesabınızı oluşturun", 22, theme.textColor, true);
        title.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams titleParams = matchWrap();
        titleParams.topMargin = dp(18);
        card.addView(title, titleParams);

        TextView subtitle = textView(
                "MetaFold Downloader kullanmak i\u00e7in e-posta hesab\u0131n\u0131zla devam edin.",
                13,
                theme.mutedColor,
                false
        );
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(dp(4), dp(4), dp(4), 0);
        card.addView(subtitle);

        EditText emailInput = authInput("E-posta", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailInput.setText(firstNonEmpty(getString(PREF_AUTH_EMAIL, ""), getString(PREF_LICENSE_EMAIL, "")));
        LinearLayout.LayoutParams emailParams = matchWrap();
        emailParams.topMargin = dp(18);
        card.addView(emailInput, emailParams);

        EditText passwordInput = authInput("\u015eifre", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        LinearLayout.LayoutParams passwordParams = matchWrap();
        passwordParams.topMargin = dp(8);
        card.addView(passwordInput, passwordParams);

        EditText repeatInput = null;
        if (!loginMode) {
            repeatInput = authInput("\u015eifre tekrar", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            LinearLayout.LayoutParams repeatParams = matchWrap();
            repeatParams.topMargin = dp(8);
            card.addView(repeatInput, repeatParams);
        }

        final EditText finalRepeatInput = repeatInput;
        Button primary = primaryButton(loginMode ? "Giri\u015f yap" : "Kay\u0131t ol", theme.accentColor);
        primary.setOnClickListener(v -> authenticateWithFirebase(
                !loginMode,
                emailInput.getText().toString(),
                passwordInput.getText().toString(),
                finalRepeatInput == null ? "" : finalRepeatInput.getText().toString()
        ));
        LinearLayout.LayoutParams primaryParams = matchWrap();
        primaryParams.topMargin = dp(12);
        card.addView(primary, primaryParams);

        Button buyLicense = primaryButton("Lisans satın al (WhatsApp)", Color.rgb(37, 211, 102));
        buyLicense.setOnClickListener(v -> openWhatsAppLicensePurchase(emailInput.getText().toString()));
        LinearLayout.LayoutParams buyParams = matchWrap();
        buyParams.topMargin = dp(8);
        card.addView(buyLicense, buyParams);

        Button switchMode = secondaryButton(loginMode ? "Hesab\u0131n\u0131z yok mu? Kay\u0131t ol" : "Zaten hesab\u0131n\u0131z var m\u0131? Giri\u015f yap");
        switchMode.setOnClickListener(v -> renderAuthPanel(panel, !loginMode));
        LinearLayout.LayoutParams switchParams = matchWrap();
        switchParams.topMargin = dp(8);
        card.addView(switchMode, switchParams);

        if (loginMode) {
            Button reset = secondaryButton("\u015eifremi unuttum");
            reset.setOnClickListener(v -> sendPasswordReset(emailInput.getText().toString()));
            LinearLayout.LayoutParams resetParams = matchWrap();
            resetParams.topMargin = dp(8);
            card.addView(reset, resetParams);
        }

        TextView footnote = textView(
                "Kay\u0131t iste\u011fi olu\u015ftuktan sonra kullan\u0131m i\u00e7in y\u00f6netici onay\u0131 gerekir.",
                12,
                theme.mutedColor,
                false
        );
        footnote.setGravity(Gravity.CENTER);
        footnote.setPadding(dp(4), dp(12), dp(4), 0);
        card.addView(footnote);
    }

    private EditText authInput(String hint, int inputType) {
        AppThemeOption theme = currentTheme();
        EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setInputType(inputType);
        input.setHint(ui(hint));
        input.setTextColor(theme.textColor);
        input.setHintTextColor(theme.mutedColor);
        input.setTextSize(15);
        input.setPadding(dp(12), dp(10), dp(12), dp(10));
        input.setBackground(rounded(theme.surfaceAltColor, 8, theme.borderColor));
        return input;
    }

    private View createAppHeader() {
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(0, 0, 0, dp(16));

        Button menuButton = secondaryButton("☰");
        menuButton.setTextSize(22);
        menuButton.setMinWidth(dp(52));
        menuButton.setOnClickListener(v -> openDrawer());
        header.addView(menuButton, new LinearLayout.LayoutParams(dp(54), dp(50)));

        LinearLayout titleBlock = new LinearLayout(this);
        titleBlock.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        titleParams.leftMargin = dp(12);
        header.addView(titleBlock, titleParams);

        TextView title = textView("MetaFold Downloader", 24, currentTheme().textColor, true);
        title.setSingleLine(true);
        title.setEllipsize(TextUtils.TruncateAt.END);
        titleBlock.addView(title);

        TextView subtitle = textView("Sosyal platformlar tek uygulamada", 13, currentTheme().mutedColor, false);
        subtitle.setSingleLine(true);
        subtitle.setEllipsize(TextUtils.TruncateAt.END);
        titleBlock.addView(subtitle);

        return header;
    }

    private void updatePlatformHeader() {
        AppThemeOption theme = currentTheme();
        if (activePlatformIcon != null) {
            activePlatformIcon.setImageResource(R.drawable.ic_settings);
            activePlatformIcon.setColorFilter(theme.accentColor);
            activePlatformIcon.setBackground(rounded(theme.surfaceColor, 8, theme.borderColor));
            activePlatformIcon.setPadding(dp(7), dp(7), dp(7), dp(7));
        }
        if (activePlatformNameView != null) {
            activePlatformNameView.setText(ui("Temalar"));
            activePlatformNameView.setTextColor(theme.accentColor);
        }
        if (activePlatformUrlView != null) {
            activePlatformUrlView.setText(themeName(theme));
        }
        if (activePlatformCard != null) {
            activePlatformCard.setBackground(rounded(softAccent(theme.accentColor), 8, theme.accentColor));
        }
        applyActivePlatformTheme();
    }

    private void applyActivePlatformTheme() {
        int accent = activePlatform.accentColor;
        if (downloadButton != null) {
            setButtonColor(downloadButton, accent);
        }
        if (browserDownloadButton != null) {
            setButtonColor(browserDownloadButton, accent);
        }
        if (platformView != null) {
            platformView.setTextColor(accent);
        }
        if (progressBar != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.getProgressDrawable().setTint(accent);
            progressBar.getIndeterminateDrawable().setTint(accent);
        }
    }

    private FrameLayout createDrawerOverlay() {
        AppThemeOption theme = currentTheme();
        FrameLayout overlay = new FrameLayout(this);
        overlay.setBackgroundColor(Color.argb(130, 0, 0, 0));
        overlay.setOnClickListener(v -> closeDrawer());

        ScrollView drawerScroll = new ScrollView(this);
        drawerScroll.setFillViewport(true);
        drawerScroll.setClickable(true);
        drawerScroll.setOnClickListener(v -> {
            // Consume clicks inside the drawer.
        });

        int drawerWidth = Math.min(dp(328), getResources().getDisplayMetrics().widthPixels - dp(44));
        FrameLayout.LayoutParams drawerParams = new FrameLayout.LayoutParams(
                drawerWidth,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        drawerParams.gravity = Gravity.LEFT;
        overlay.addView(drawerScroll, drawerParams);

        drawerPanel = new LinearLayout(this);
        drawerPanel.setOrientation(LinearLayout.VERTICAL);
        drawerPanel.setPadding(dp(18), dp(22), dp(18), dp(18));
        drawerPanel.setBackgroundColor(theme.surfaceColor);
        drawerScroll.addView(drawerPanel, new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.WRAP_CONTENT
        ));

        LinearLayout headerRow = new LinearLayout(this);
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setGravity(Gravity.CENTER_VERTICAL);
        drawerPanel.addView(headerRow, matchWrap());

        LinearLayout titleBlock = new LinearLayout(this);
        titleBlock.setOrientation(LinearLayout.VERTICAL);
        headerRow.addView(titleBlock, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        TextView title = textView("MetaFold", 24, theme.textColor, true);
        titleBlock.addView(title);
        TextView subtitle = textView("Downloader", 13, theme.mutedColor, false);
        titleBlock.addView(subtitle);

        Button close = secondaryButton("×");
        close.setTextSize(22);
        close.setOnClickListener(v -> closeDrawer());
        headerRow.addView(close, new LinearLayout.LayoutParams(dp(46), dp(44)));

        addDrawerItem("Ana ekran", "Tanıtım ve hızlı kısayollar", R.drawable.ic_home, () -> {
            closeDrawer();
            showHome();
        });

        addDrawerSection("Platformlar");
        for (SocialPlatform platform : PLATFORMS) {
            addDrawerItem(platform.name, "Akışı aç", platform.iconRes, () -> {
                closeDrawer();
                openPlatform(platform);
            });
        }
        addDrawerItem("Link ile indir", "Paylaşılan bağlantıdan seçenekleri getir", R.drawable.ic_link, () -> {
            closeDrawer();
            showDownloader();
        });

        addDrawerSection("Menü");
        addDrawerItem("İndirilenler", "Dosyaları aç veya paylaş", R.drawable.ic_downloads, () -> {
            closeDrawer();
            showDownloads();
        });
        addDrawerItem("Aktif platform akışı", "Seçili platformda gezin", R.drawable.ic_link, () -> {
            closeDrawer();
            openPlatform(activePlatform);
        });
        addDrawerItem("Ayarlar", "Kalite, dil, görünüm ve bildirimler", R.drawable.ic_settings, () -> {
            closeDrawer();
            showSettings();
        });
        addDrawerItem("Hakkında", "Ahmet Doğan ve MetaFold bilgileri", R.drawable.ic_about, () -> {
            closeDrawer();
            showAbout();
        });

        addDrawerSection("Temalar");
        activePlatformCard = new LinearLayout(this);
        activePlatformCard.setOrientation(LinearLayout.HORIZONTAL);
        activePlatformCard.setGravity(Gravity.CENTER_VERTICAL);
        activePlatformCard.setPadding(dp(12), dp(12), dp(12), dp(12));
        activePlatformCard.setBackground(rounded(softAccent(theme.accentColor), 8, theme.accentColor));
        activePlatformCard.setOnClickListener(v -> showThemeChooser());
        LinearLayout.LayoutParams activeParams = matchWrap();
        activeParams.topMargin = dp(8);
        drawerPanel.addView(activePlatformCard, activeParams);

        activePlatformIcon = new ImageView(this);
        activePlatformCard.addView(activePlatformIcon, new LinearLayout.LayoutParams(dp(40), dp(40)));

        LinearLayout activeTexts = new LinearLayout(this);
        activeTexts.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams activeTextParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        activeTextParams.leftMargin = dp(12);
        activePlatformCard.addView(activeTexts, activeTextParams);

        activePlatformNameView = textView("", 17, theme.textColor, true);
        activeTexts.addView(activePlatformNameView);
        activePlatformUrlView = textView("", 12, theme.mutedColor, false);
        activeTexts.addView(activePlatformUrlView);

        updatePlatformHeader();
        return overlay;
    }

    private void openDrawer() {
        if (!ensureNoMandatoryUpdate()) {
            return;
        }
        if (authPanel != null && authPanel.getVisibility() == View.VISIBLE) {
            return;
        }
        if (drawerOverlay != null) {
            drawerOverlay.animate().cancel();
            if (drawerPanel != null) {
                drawerPanel.animate().cancel();
                int width = drawerPanel.getWidth() > 0 ? drawerPanel.getWidth() : Math.min(dp(328), getResources().getDisplayMetrics().widthPixels - dp(44));
                drawerPanel.setTranslationX(-width);
            }
            drawerOverlay.setAlpha(0f);
            drawerOverlay.setVisibility(View.VISIBLE);
            drawerOverlay.bringToFront();
            drawerOverlay.animate().alpha(1f).setDuration(180L).start();
            if (drawerPanel != null) {
                drawerPanel.animate().translationX(0f).setDuration(220L).start();
            }
        }
    }

    private void closeDrawer() {
        if (drawerOverlay != null) {
            drawerOverlay.animate().cancel();
            if (drawerPanel != null) {
                drawerPanel.animate().cancel();
                int width = drawerPanel.getWidth() > 0 ? drawerPanel.getWidth() : Math.min(dp(328), getResources().getDisplayMetrics().widthPixels - dp(44));
                drawerPanel.animate().translationX(-width).setDuration(180L).start();
            }
            drawerOverlay.animate()
                    .alpha(0f)
                    .setDuration(180L)
                    .withEndAction(() -> {
                        drawerOverlay.setVisibility(View.GONE);
                        drawerOverlay.setAlpha(1f);
                        if (drawerPanel != null) {
                            drawerPanel.setTranslationX(0f);
                        }
                    })
                    .start();
        }
    }

    private void forceDrawerClosed() {
        if (drawerOverlay != null) {
            drawerOverlay.animate().cancel();
            drawerOverlay.setVisibility(View.GONE);
            drawerOverlay.setAlpha(1f);
        }
        if (drawerPanel != null) {
            drawerPanel.animate().cancel();
            int width = drawerPanel.getWidth() > 0 ? drawerPanel.getWidth() : Math.min(dp(328), getResources().getDisplayMetrics().widthPixels - dp(44));
            drawerPanel.setTranslationX(-width);
        }
    }

    private void addDrawerSection(String title) {
        TextView section = textView(title, 12, Color.rgb(96, 112, 106), true);
        section.setPadding(0, dp(18), 0, dp(6));
        drawerPanel.addView(section);
    }

    private void addDrawerItem(String title, String subtitle, int iconRes, Runnable action) {
        AppThemeOption theme = currentTheme();
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(10), dp(10), dp(10), dp(10));
        row.setBackground(rounded(theme.surfaceColor, 8, theme.borderColor));
        row.setOnClickListener(v -> action.run());

        ImageView icon = new ImageView(this);
        icon.setImageResource(iconRes);
        if (shouldTintGenericIcon(iconRes)) {
            icon.setColorFilter(theme.mutedColor);
        }
        row.addView(icon, new LinearLayout.LayoutParams(dp(32), dp(32)));

        LinearLayout texts = new LinearLayout(this);
        texts.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        textParams.leftMargin = dp(12);
        row.addView(texts, textParams);

        TextView name = textView(title, 15, Color.rgb(23, 32, 29), true);
        texts.addView(name);

        TextView detail = textView(subtitle, 12, Color.rgb(96, 112, 106), false);
        detail.setSingleLine(true);
        detail.setEllipsize(TextUtils.TruncateAt.END);
        texts.addView(detail);

        LinearLayout.LayoutParams params = matchWrap();
        params.topMargin = dp(6);
        drawerPanel.addView(row, params);
    }

    private void showPlatformChooser() {
        String[] names = new String[PLATFORMS.length];
        for (int i = 0; i < PLATFORMS.length; i++) {
            names[i] = PLATFORMS[i].name;
        }
        new AlertDialog.Builder(this)
                .setTitle(ui("Platform seç"))
                .setItems(names, (dialog, which) -> openPlatform(PLATFORMS[which]))
                .show();
    }

    private void showThemeChooser() {
        String[] labels = new String[APP_THEMES.length];
        int checked = 0;
        String current = currentTheme().key;
        for (int i = 0; i < APP_THEMES.length; i++) {
            labels[i] = themeName(APP_THEMES[i]) + " - " + themeSubtitle(APP_THEMES[i]);
            if (APP_THEMES[i].key.equals(current)) {
                checked = i;
            }
        }
        new AlertDialog.Builder(this)
                .setTitle(ui("Tema seç"))
                .setSingleChoiceItems(labels, checked, (dialog, which) -> {
                    putString(PREF_THEME, APP_THEMES[which].key);
                    toast("Tema değiştirildi");
                    dialog.dismiss();
                    recreate();
                })
                .setNegativeButton(ui("Vazgeç"), null)
                .show();
    }

    private View createPlatformTabs() {
        HorizontalScrollView scroll = new HorizontalScrollView(this);
        scroll.setHorizontalScrollBarEnabled(false);

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        scroll.addView(row, new HorizontalScrollView.LayoutParams(
                HorizontalScrollView.LayoutParams.WRAP_CONTENT,
                HorizontalScrollView.LayoutParams.WRAP_CONTENT
        ));

        addNavItem(row, "Ana", R.drawable.ic_home, () -> showHome());
        addNavItem(row, "Link", R.drawable.ic_link, () -> showDownloader());
        addNavItem(row, "Akış", R.drawable.ic_link, () -> openPlatform(activePlatform));
        addNavItem(row, "İndirilenler", R.drawable.ic_downloads, () -> showDownloads());
        addNavItem(row, "Ayarlar", R.drawable.ic_settings, () -> showSettings());
        addNavItem(row, "Hakkında", R.drawable.ic_about, () -> showAbout());
        return scroll;
    }

    private LinearLayout createBrowserPanel() {
        AppThemeOption theme = currentTheme();
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(dp(10), dp(10), dp(10), dp(10));
        panel.setBackground(rounded(theme.surfaceColor, 8, theme.borderColor));

        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setOrientation(LinearLayout.HORIZONTAL);
        panel.addView(toolbar, matchWrap());

        Button backButton = secondaryButton("Geri");
        backButton.setOnClickListener(v -> {
            if (webView != null && webView.canGoBack()) {
                webView.goBack();
            }
        });
        toolbar.addView(backButton, rowWeight());

        browserDownloadButton = primaryButton("Videoyu indir", Color.rgb(8, 127, 111));
        browserDownloadButton.setEnabled(false);
        browserDownloadButton.setOnClickListener(v -> useCurrentBrowserPage());
        LinearLayout.LayoutParams currentParams = rowWeight();
        currentParams.leftMargin = dp(8);
        toolbar.addView(browserDownloadButton, currentParams);

        browserUrlView = textView("", 12, Color.rgb(96, 112, 106), false);
        browserUrlView.setSingleLine(true);
        browserUrlView.setEllipsize(TextUtils.TruncateAt.END);
        browserUrlView.setPadding(0, dp(10), 0, dp(8));
        panel.addView(browserUrlView);

        webView = new WebView(this);
        panel.addView(webView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(560)
        ));

        return panel;
    }

    private LinearLayout createDownloadsPanel() {
        AppThemeOption theme = currentTheme();
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(dp(14), dp(14), dp(14), dp(14));
        panel.setBackground(rounded(theme.surfaceColor, 8, theme.borderColor));

        TextView header = textView("İndirilenler", 21, Color.rgb(23, 32, 29), true);
        panel.addView(header);

        TextView path = textView("Dosyalar: Downloads/" + DOWNLOAD_FOLDER, 13, Color.rgb(96, 112, 106), false);
        path.setPadding(0, dp(6), 0, dp(10));
        panel.addView(path);

        downloadSearchInput = new EditText(this);
        downloadSearchInput.setHint(ui("İndirilenlerde ara"));
        downloadSearchInput.setSingleLine(true);
        downloadSearchInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        downloadSearchInput.setTextColor(theme.textColor);
        downloadSearchInput.setHintTextColor(theme.mutedColor);
        downloadSearchInput.setTextSize(14);
        downloadSearchInput.setPadding(dp(12), dp(10), dp(12), dp(10));
        downloadSearchInput.setBackground(rounded(theme.surfaceAltColor, 8, theme.borderColor));
        downloadSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                clearDownloadSelection(false);
                refreshDownloads();
            }
        });
        panel.addView(downloadSearchInput, matchWrap());

        LinearLayout listActions = new LinearLayout(this);
        listActions.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams listActionParams = matchWrap();
        listActionParams.topMargin = dp(8);
        panel.addView(listActions, listActionParams);

        Button refreshButton = secondaryButton("Listeyi yenile");
        refreshButton.setOnClickListener(v -> refreshDownloads());
        listActions.addView(refreshButton, rowWeight());

        Button clearListButton = secondaryButton("Listeyi temizle");
        clearListButton.setOnClickListener(v -> confirmClearDownloadsList());
        LinearLayout.LayoutParams clearListParams = rowWeight();
        clearListParams.leftMargin = dp(8);
        listActions.addView(clearListButton, clearListParams);

        downloadsSelectionBar = new LinearLayout(this);
        downloadsSelectionBar.setOrientation(LinearLayout.VERTICAL);
        downloadsSelectionBar.setPadding(dp(10), dp(10), dp(10), dp(10));
        downloadsSelectionBar.setBackground(rounded(theme.surfaceAltColor, 8, theme.borderColor));
        downloadsSelectionBar.setVisibility(View.GONE);

        downloadSelectionStatus = textView("Seçilen: 0", 13, Color.rgb(23, 32, 29), true);
        downloadsSelectionBar.addView(downloadSelectionStatus);

        LinearLayout selectionActions = new LinearLayout(this);
        selectionActions.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams selectionActionParams = matchWrap();
        selectionActionParams.topMargin = dp(8);
        downloadsSelectionBar.addView(selectionActions, selectionActionParams);

        selectAllDownloadsButton = secondaryButton("Tümünü seç");
        selectAllDownloadsButton.setOnClickListener(v -> toggleSelectAllVisibleDownloads());
        selectionActions.addView(selectAllDownloadsButton, rowWeight());

        deleteSelectedDownloadsButton = primaryButton("Listeden kaldır", Color.rgb(198, 40, 40));
        deleteSelectedDownloadsButton.setOnClickListener(v -> confirmRemoveSelectedDownloadsFromApp());
        LinearLayout.LayoutParams deleteParams = rowWeight();
        deleteParams.leftMargin = dp(8);
        selectionActions.addView(deleteSelectedDownloadsButton, deleteParams);

        Button cancelSelectionButton = secondaryButton("Bitti");
        cancelSelectionButton.setOnClickListener(v -> clearDownloadSelection(true));
        LinearLayout.LayoutParams cancelSelectionParams = rowWeight();
        cancelSelectionParams.leftMargin = dp(8);
        selectionActions.addView(cancelSelectionButton, cancelSelectionParams);

        LinearLayout.LayoutParams selectionParams = matchWrap();
        selectionParams.topMargin = dp(8);
        panel.addView(downloadsSelectionBar, selectionParams);

        downloadsList = new LinearLayout(this);
        downloadsList.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams listParams = matchWrap();
        listParams.topMargin = dp(10);
        panel.addView(downloadsList, listParams);

        return panel;
    }

    private LinearLayout createSettingsPanel() {
        AppThemeOption theme = currentTheme();
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(dp(14), dp(14), dp(14), dp(14));
        panel.setBackground(rounded(theme.surfaceColor, 8, theme.borderColor));
        renderSettingsHome(panel);
        return panel;
    }

    private void renderSettingsHome(LinearLayout panel) {
        panel.removeAllViews();
        animateSettingsPanel(panel, false);
        TextView header = textView("Ayarlar", 21, Color.rgb(23, 32, 29), true);
        panel.addView(header);

        addSettingsCategory(panel, "Video ve ses", "Varsayılan kalite, format, ses tercihleri", () -> renderVideoAudioSettings(panel));
        addSettingsCategory(panel, "İndir", "Klasör, dosya adı, deneme ve kuyruk ayarları", () -> renderDownloadSettings(panel));
        addSettingsCategory(panel, "Görünüm", "Tema, liste ve sekme tercihleri", () -> renderAppearanceSettings(panel));
        addSettingsCategory(panel, "Veri ve önbellek", "Geçici dosyaları ve çerezleri temizle", () -> renderHistoryCacheSettings(panel));
        addSettingsCategory(panel, "İçerik", "Dil ve platform içerik davranışı", () -> renderContentSettings(panel));
        addSettingsCategory(panel, "Güncellemeler", "Yeni sürüm bildirimi ve elle denetleme", () -> renderUpdateSettings(panel));
        addSettingsCategory(panel, "Lisans", "Lisans anahtarı ve cihaz doğrulaması", () -> renderLicenseSettings(panel));
        addActionSetting(panel, "Tam ekranı tekrar uygula", "Bildirim ve gezinme çubuklarını yeniden gizle", () -> {
            enterFullscreen();
            toast("Tam ekran yenilendi");
        });
        if (isSignedIn()) {
            addActionSetting(panel, "Çıkış yap", "Bu cihazdaki oturumu ve lisans bilgisini temizle", () -> confirm(
                    "Çıkış yap",
                    "Bu cihazdaki oturum kapatılsın mı?",
                    this::signOut
            ));
        }
    }

    private void renderVideoAudioSettings(LinearLayout panel) {
        renderSettingsHeader(panel, "Video ve ses");
        addChoiceSetting(panel, "Varsayılan çözünürlük", PREF_DEFAULT_RESOLUTION, "720p",
                new String[]{"En iyi", "480p", "720p", "1080p", "2K", "4K"});
        addSwitchSetting(panel, "Yüksek çözünürlükleri göster", "2K/4K seçeneklerini kalite listesinde göster", PREF_SHOW_HIGH_RESOLUTION, true);
        addChoiceSetting(panel, "Varsayılan video biçimi", PREF_VIDEO_FORMAT, "MPEG-4",
                new String[]{"MPEG-4", "WebM"});
        addChoiceSetting(panel, "Varsayılan ses biçimi", PREF_AUDIO_FORMAT, "MP3",
                new String[]{"MP3", "M4A", "Opus"});
        addInfoSetting(panel, "Kalite sıralaması", "Seçenekler videonun en yüksek kalitesinden aşağı sıralanır");
    }

    private void renderDownloadSettings(LinearLayout panel) {
        renderSettingsHeader(panel, "İndir");
        addActionSetting(panel, "Video indirme klasörü", folderLabel(PREF_VIDEO_FOLDER_URI), () -> chooseDownloadFolder(REQUEST_VIDEO_FOLDER));
        addActionSetting(panel, "Ses indirme klasörü", folderLabel(PREF_AUDIO_FOLDER_URI), () -> chooseDownloadFolder(REQUEST_AUDIO_FOLDER));
        addChoiceSetting(panel, "Dosya adlarında izin verilen karakterler", PREF_FILENAME_CHARS, "Çoğu özel karakterler",
                new String[]{"Çoğu özel karakterler", "Kısıtlı karakterler", "ASCII güvenli"});
        addChoiceSetting(panel, "Değiştirme karakteri", PREF_REPLACEMENT_CHAR, "_",
                new String[]{"_", "-", "."});
        addChoiceSetting(panel, "Azami deneme sayısı", PREF_MAX_RETRIES, "10",
                new String[]{"3", "5", "10", "20"});
        addSwitchSetting(panel, "İndirme kuyruğu", "İndirme sürerken yeni seçimleri sıraya ekle", PREF_LIMIT_QUEUE, true);
    }

    private void renderAppearanceSettings(LinearLayout panel) {
        renderSettingsHeader(panel, "Görünüm");
        addChoiceSetting(panel, "Tema", PREF_THEME, APP_THEMES[0].key, themeKeys(), value -> {
            putString(PREF_THEME, value);
            toast("Tema değiştirildi");
            recreate();
        }, this::themeName);
        addSwitchSetting(panel, "Yüksek yenileme hızı", "Ekran destekliyorsa 90/120 Hz modunu kullan", PREF_HIGH_REFRESH_RATE, true,
                enabled -> applyRefreshRatePreference());
        addInfoSetting(panel, "Platform renkleri", "Platform logoları ve indirilen kartları kendi marka rengiyle görünür");
        addInfoSetting(panel, "Menü animasyonu", "Sol menü kayarak açılır ve kapanır");
    }

    private void renderHistoryCacheSettings(LinearLayout panel) {
        renderSettingsHeader(panel, "Veri ve önbellek");
        addActionSetting(panel, "Önbelleğe alınmış meta verileri sil", "Tüm geçici web ve video verilerini kaldır", () -> confirm(
                "Önbelleği temizle",
                "Geçici video ve web verileri silinsin mi?",
                () -> {
            deleteChildren(getCacheDir());
            toast("Önbellek temizlendi");
                }
        ));
        addActionSetting(panel, "Web oturum çerezlerini temizle", "Uygulama içi platform girişlerini sıfırla", () -> confirm(
                "Çerezleri temizle",
                "Platform girişleri ve oturum çerezleri silinsin mi?",
                () -> {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
            toast("Çerezler temizlendi");
                }
        ));
    }

    private void renderContentSettings(LinearLayout panel) {
        renderSettingsHeader(panel, "İçerik");
        addChoiceSetting(panel, "Uygulama dili", PREF_LANGUAGE, selectedLanguage,
                new String[]{"tr", "en"}, value -> {
                    selectedLanguage = value;
                    putString(PREF_LANGUAGE, value);
                    toast("Dil değiştirildi");
                    recreate();
                }, value -> "tr".equals(value) ? "Türkçe" : "English");
        addActionSetting(panel, "Oturumlu içerik", "Seçili platformda giriş sayfasını aç", () -> openPlatform(activePlatform));
        addActionSetting(panel, "Platform akışı", "Seçili platformu uygulama içinde aç", () -> openPlatform(activePlatform));
    }

    private void renderUpdateSettings(LinearLayout panel) {
        renderSettingsHeader(panel, "Güncellemeler");
        addSwitchSetting(panel, "Güncellemeler", "Yeni sürüm olduğunda uygulama güncellemesi için bildirim göster", PREF_UPDATE_NOTIFICATIONS, false);
        addActionSetting(panel, "Uygulama güncellemesini denetle", "GitHub Releases üzerinden yeni APK sürümünü kontrol et", () -> checkAppUpdate(false));
        addActionSetting(panel, "GitHub release sayfasını aç", "Yeni APK dosyaları bu repodaki release bölümünden alınır", () -> openWebsite(GITHUB_RELEASES_URL));
        addActionSetting(panel, "İndirme motorunu güncelle", "yt-dlp çekirdeğini son kararlı sürüme yükselt", () -> updateExtractor());
        addInfoSetting(panel, "Güncelleme kaynağı", "metafold-dev/metafold-downloader-android");
        addInfoSetting(panel, "Uygulama sürümü", "MetaFold Downloader " + currentAppVersion());
        addInfoSetting(panel, "İndirme motoru sürümü", safeVersionName());
    }

    private void renderLicenseSettings(LinearLayout panel) {
        renderSettingsHeader(panel, "Lisans");
        addInfoSetting(panel, "Oturum", TextUtils.isEmpty(currentAuthEmail()) ? "Giriş yapılmadı" : currentAuthEmail());
        addInfoSetting(panel, "Lisans durumu", licenseStatusText());
        addInfoSetting(panel, "Lisans bitişi", licenseExpiryLabel());
        addInfoSetting(panel, "Kayıt e-postası", licenseEmailLabel());
        addInfoSetting(panel, "Cihaz kimliği", shortDeviceId());
        addInfoSetting(panel, "Lisanslı cihaz", licenseDeviceLabel());
        addInfoSetting(panel, "Cihaz değişim kilidi", licenseDeviceChangeLabel());
        addActionSetting(panel, "Lisans satın al (WhatsApp)", "Ahmet Doğan'a lisans isteği gönder", () -> openWhatsAppLicensePurchase(currentAuthEmail()));
        if (!isSignedIn()) {
            addActionSetting(panel, "Kayıt / giriş ekranı", "E-posta ve şifre ile oturum aç", () -> showAuthPanel(false));
        } else {
            addActionSetting(panel, "Onay isteğini yenile", "Kullanım isteği oluştur ve yönetici onayı bekle", () -> registerLicenseEmail(currentAuthEmail(), panel));
        }
        addActionSetting(panel, "Lisans anahtarı gir", licenseKeyLabel(), () -> showLicenseDialog(panel));
        addActionSetting(panel, "Onay durumunu kontrol et", "Sunucudan lisans/onay durumunu denetle", () -> validateLicense(false, panel));
        if (!TextUtils.isEmpty(getString(PREF_LICENSE_KEY, "")) || !TextUtils.isEmpty(getString(PREF_LICENSE_EMAIL, ""))) {
            addActionSetting(panel, "Lisansı kaldır", "Bu cihazdaki kayıtlı lisans anahtarını temizle", () -> confirm(
                    "Lisansı kaldır",
                    "Kayıtlı lisans bilgileri bu cihazdan silinsin mi?",
                    () -> {
                        clearLicense();
                        renderLicenseSettings(panel);
                    }
            ));
        }
        addInfoSetting(panel, "Doğrulama modeli", "E-posta/lisans anahtarı + cihaz kimliği sunucuda onaylanır; onay yoksa indirme kullanılamaz");
    }

    private void renderNotificationSettings(LinearLayout panel) {
        renderSettingsHeader(panel, "Bildirimler");
        addSwitchSetting(panel, "Oynatıcı bildirimi", "Oynatılan akış bildirimini yapılandır", PREF_PLAYER_NOTIFICATION, true);
        addSwitchSetting(panel, "Yeni akış bildirimleri", "Aboneliklerden yeni akışlarla ilgili bildirim gönder", PREF_NEW_FEED_NOTIFICATIONS, false);
        addChoiceSetting(panel, "Denetleme sıklığı", PREF_NOTIFICATION_CHECK_FREQUENCY, "4 saat",
                new String[]{"1 saat", "4 saat", "12 saat", "24 saat"});
        addChoiceSetting(panel, "Gerekli ağ bağlantısı", PREF_NOTIFICATION_NETWORK, "Yalnızca Wi-Fi",
                new String[]{"Yalnızca Wi-Fi", "Herhangi bir ağ"});
        addInfoSetting(panel, "Kanallar", getString(PREF_NOTIFICATION_CHANNELS, "0/0"));
    }

    private void renderSettingsHeader(LinearLayout panel, String title) {
        panel.removeAllViews();
        animateSettingsPanel(panel, true);
        LinearLayout headerRow = new LinearLayout(this);
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setGravity(Gravity.CENTER_VERTICAL);
        panel.addView(headerRow, matchWrap());

        Button back = secondaryButton("<");
        back.setMinWidth(dp(48));
        back.setOnClickListener(v -> renderSettingsHome(panel));
        headerRow.addView(back, new LinearLayout.LayoutParams(dp(48), dp(44)));

        TextView header = textView(title, 21, Color.rgb(23, 32, 29), true);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        titleParams.leftMargin = dp(10);
        headerRow.addView(header, titleParams);
    }

    private void animateSettingsPanel(View panel, boolean forward) {
        if (panel == null) {
            return;
        }
        panel.animate().cancel();
        panel.setAlpha(0f);
        panel.setTranslationX(forward ? dp(22) : -dp(14));
        panel.animate().alpha(1f).translationX(0f).setDuration(180L).start();
    }

    private void addSettingsCategory(LinearLayout panel, String title, String subtitle, Runnable action) {
        LinearLayout row = settingBaseRow();
        row.setOnClickListener(v -> action.run());
        row.addView(settingTextBlock(title, subtitle), rowTextParams());

        TextView arrow = textView(">", 22, Color.rgb(96, 112, 106), true);
        arrow.setGravity(Gravity.CENTER);
        row.addView(arrow, new LinearLayout.LayoutParams(dp(28), LinearLayout.LayoutParams.WRAP_CONTENT));
        addSettingRow(panel, row);
    }

    private void addChoiceSetting(LinearLayout panel, String title, String key, String defaultValue, String[] values) {
        addChoiceSetting(panel, title, key, defaultValue, values, value -> {
            putString(key, value);
            renderCurrentSettingsPage(panel, title);
        }, value -> value);
    }

    private void addChoiceSetting(LinearLayout panel, String title, String key, String defaultValue, String[] values, SettingValueHandler handler, ValueLabelFormatter formatter) {
        String current = getString(key, defaultValue);
        LinearLayout row = settingBaseRow();
        row.setOnClickListener(v -> {
            String active = getString(key, defaultValue);
            String[] labels = new String[values.length];
            int checked = -1;
            for (int i = 0; i < values.length; i++) {
                labels[i] = ui(formatter.label(values[i]));
                if (values[i].equals(active)) {
                    checked = i;
                }
            }
            new AlertDialog.Builder(this)
                    .setTitle(ui(title))
                    .setSingleChoiceItems(labels, checked, (dialog, which) -> {
                        handler.onValue(values[which]);
                        dialog.dismiss();
                    })
                    .setNegativeButton(ui("Vazgeç"), null)
                    .show();
        });
        row.addView(settingTextBlock(title, formatter.label(current)), rowTextParams());

        TextView value = textView(formatter.label(current), 13, currentTheme().accentColor, true);
        value.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        row.addView(value, new LinearLayout.LayoutParams(dp(104), LinearLayout.LayoutParams.WRAP_CONTENT));
        addSettingRow(panel, row);
    }

    private void addSwitchSetting(LinearLayout panel, String title, String subtitle, String key, boolean defaultValue) {
        addSwitchSetting(panel, title, subtitle, key, defaultValue, null);
    }

    private void addSwitchSetting(LinearLayout panel, String title, String subtitle, String key, boolean defaultValue, SwitchValueHandler handler) {
        LinearLayout row = settingBaseRow();
        row.addView(settingTextBlock(title, subtitle), rowTextParams());

        Switch toggle = new Switch(this);
        toggle.setChecked(getBool(key, defaultValue));
        toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            putBool(key, isChecked);
            if (handler != null) {
                handler.onValue(isChecked);
            }
        });
        row.setOnClickListener(v -> toggle.setChecked(!toggle.isChecked()));
        row.addView(toggle, new LinearLayout.LayoutParams(dp(74), LinearLayout.LayoutParams.WRAP_CONTENT));
        addSettingRow(panel, row);
    }

    private void addActionSetting(LinearLayout panel, String title, String subtitle, Runnable action) {
        LinearLayout row = settingBaseRow();
        row.setOnClickListener(v -> action.run());
        row.addView(settingTextBlock(title, subtitle), rowTextParams());
        addSettingRow(panel, row);
    }

    private void addInfoSetting(LinearLayout panel, String title, String subtitle) {
        LinearLayout row = settingBaseRow();
        row.addView(settingTextBlock(title, subtitle), rowTextParams());
        addSettingRow(panel, row);
    }

    private void addSectionLabel(LinearLayout panel, String title) {
        TextView label = textView(title, 14, Color.rgb(23, 32, 29), true);
        label.setPadding(0, dp(16), 0, dp(4));
        panel.addView(label);
    }

    private LinearLayout settingBaseRow() {
        AppThemeOption theme = currentTheme();
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(10), dp(10), dp(10), dp(10));
        row.setBackground(rounded(theme.surfaceAltColor, 8, theme.borderColor));
        return row;
    }

    private LinearLayout settingTextBlock(String title, String subtitle) {
        LinearLayout texts = new LinearLayout(this);
        texts.setOrientation(LinearLayout.VERTICAL);

        TextView titleView = textView(title, 15, Color.rgb(23, 32, 29), false);
        texts.addView(titleView);

        if (!TextUtils.isEmpty(subtitle)) {
            TextView subtitleView = textView(subtitle, 12, Color.rgb(96, 112, 106), false);
            subtitleView.setPadding(0, dp(2), 0, 0);
            texts.addView(subtitleView);
        }
        return texts;
    }

    private LinearLayout.LayoutParams rowTextParams() {
        return new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
    }

    private void addSettingRow(LinearLayout panel, View row) {
        LinearLayout.LayoutParams params = matchWrap();
        params.topMargin = dp(8);
        panel.addView(row, params);
    }

    private void renderCurrentSettingsPage(LinearLayout panel, String changedTitle) {
        CharSequence header = "";
        if (panel.getChildCount() > 0 && panel.getChildAt(0) instanceof LinearLayout) {
            LinearLayout headerRow = (LinearLayout) panel.getChildAt(0);
            if (headerRow.getChildCount() > 1 && headerRow.getChildAt(1) instanceof TextView) {
                header = ((TextView) headerRow.getChildAt(1)).getText();
            }
        }
        String page = String.valueOf(header);
        if ("Video ve ses".equals(page)) {
            renderVideoAudioSettings(panel);
        } else if ("İndir".equals(page)) {
            renderDownloadSettings(panel);
        } else if ("Görünüm".equals(page)) {
            renderAppearanceSettings(panel);
        } else if ("Veri ve önbellek".equals(page)) {
            renderHistoryCacheSettings(panel);
        } else if ("İçerik".equals(page)) {
            renderContentSettings(panel);
        } else if ("Güncellemeler".equals(page)) {
            renderUpdateSettings(panel);
        } else if ("Lisans".equals(page)) {
            renderLicenseSettings(panel);
        } else if ("Bildirimler".equals(page)) {
            renderNotificationSettings(panel);
        } else {
            renderSettingsHome(panel);
        }
    }

    private String nextValue(String[] values, String current, String defaultValue) {
        if (values.length == 0) {
            return defaultValue;
        }
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(current)) {
                return values[(i + 1) % values.length];
            }
        }
        return values[0];
    }

    private String getString(String key, String defaultValue) {
        return getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString(key, defaultValue);
    }

    private boolean getBool(String key, boolean defaultValue) {
        return getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    private void putString(String key, String value) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().putString(key, value).apply();
    }

    private void putBool(String key, boolean value) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().putBoolean(key, value).apply();
    }

    private void deleteChildren(File directory) {
        if (directory == null || !directory.isDirectory()) {
            return;
        }
        File[] children = directory.listFiles();
        if (children == null) {
            return;
        }
        for (File child : children) {
            if (child.isDirectory()) {
                deleteChildren(child);
            }
            if (!child.delete()) {
                child.deleteOnExit();
            }
        }
    }

    private LinearLayout createAboutPanel() {
        AppThemeOption theme = currentTheme();
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(dp(14), dp(14), dp(14), dp(14));
        panel.setBackground(rounded(theme.surfaceColor, 8, theme.borderColor));

        TextView header = textView("Hakkında", 21, Color.rgb(23, 32, 29), true);
        panel.addView(header);

        TextView body = textView(
                aboutBody(),
                14,
                Color.rgb(23, 32, 29),
                false
        );
        body.setPadding(0, dp(10), 0, 0);
        panel.addView(body);

        TextView site = textView("www.metafold.net", 16, Color.rgb(8, 127, 111), true);
        site.setPadding(0, dp(14), 0, 0);
        site.setPaintFlags(site.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);
        site.setOnClickListener(v -> openWebsite("https://www.metafold.net"));
        panel.addView(site);

        Button coffee = primaryButton("Geliştiriciye kahve ısmarla", theme.accentColor);
        coffee.setOnClickListener(v -> showDonationDialog());
        LinearLayout.LayoutParams coffeeParams = matchWrap();
        coffeeParams.topMargin = dp(14);
        panel.addView(coffee, coffeeParams);

        return panel;
    }

    private void showDonationDialog() {
        String message = donationMessage();
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(ui("Geliştiriciye kahve ısmarla"))
                .setMessage(message)
                .setNegativeButton(ui("Vazgeç"), null);

        if (!TextUtils.isEmpty(DONATION_IBAN)) {
            builder.setPositiveButton(ui("IBAN kopyala"), (dialog, which) -> copyDonationIban());
            builder.setNeutralButton(ui("Paylaş"), (dialog, which) -> shareDonationInfo());
        } else {
            builder.setPositiveButton(ui("Destek sayfasını aç"), (dialog, which) -> openWebsite("https://www.metafold.net"));
        }
        builder.show();
    }

    private String donationMessage() {
        if (TextUtils.isEmpty(DONATION_IBAN)) {
            return ui("IBAN bilgisi henüz eklenmedi. Destek hesabını bağlamak için geliştirici IBAN bilgisini eklemeli.");
        }
        if (isEnglish()) {
            return "Recipient: " + DONATION_RECIPIENT + "\n" +
                    "IBAN: " + DONATION_IBAN + "\n" +
                    "Note: " + DONATION_NOTE + "\n\n" +
                    "Tap copy, then open your banking app and paste the IBAN.";
        }
        return "Alıcı: " + DONATION_RECIPIENT + "\n" +
                "IBAN: " + DONATION_IBAN + "\n" +
                "Açıklama: " + DONATION_NOTE + "\n\n" +
                "Kopyala'ya dokunup banka uygulamanızda IBAN alanına yapıştırabilirsiniz.";
    }

    private String donationTransferText() {
        return "Alıcı: " + DONATION_RECIPIENT + "\nIBAN: " + DONATION_IBAN + "\nAçıklama: " + DONATION_NOTE;
    }

    private void copyDonationIban() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (clipboard == null || TextUtils.isEmpty(DONATION_IBAN)) {
            toast("IBAN bilgisi yok");
            return;
        }
        clipboard.setPrimaryClip(ClipData.newPlainText("MetaFold IBAN", DONATION_IBAN));
        toast("IBAN kopyalandı");
        openDonationPaymentLinkIfAvailable();
    }

    private void shareDonationInfo() {
        if (TextUtils.isEmpty(DONATION_IBAN)) {
            toast("IBAN bilgisi yok");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, donationTransferText());
        try {
            startActivity(Intent.createChooser(intent, ui("Paylaş")));
        } catch (Exception error) {
            toast("Uygun uygulama bulunamadı.");
        }
    }

    private void openDonationPaymentLinkIfAvailable() {
        if (!TextUtils.isEmpty(DONATION_PAYMENT_LINK)) {
            openWebsite(DONATION_PAYMENT_LINK);
        }
    }

    private String aboutBody() {
        if (isEnglish()) {
            return "MetaFold Downloader is a social video download helper built for the MetaFold ecosystem.\n\n" +
                    "Developer: Ahmet Doğan\n" +
                    "Version: " + currentAppVersion() + "\n\n" +
                    "The app is designed only for content you own or are authorized to download.";
        }
        return "MetaFold Downloader, MetaFold ekosistemi için geliştirilen sosyal video indirme yardımcısıdır.\n\n" +
                "Geliştirici: Ahmet Doğan\n" +
                "Sürüm: " + currentAppVersion() + "\n\n" +
                "Uygulama yalnızca size ait veya indirme yetkiniz olan içerikler için tasarlanmıştır.";
    }

    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setUserAgentString(settings.getUserAgentString() + " MetaFoldDownloader/" + currentAppVersion());
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return request != null && handleBrowserNavigation(view, request.getUrl());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return handleBrowserNavigation(view, url == null ? null : Uri.parse(url));
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                flushWebCookies();
                updateBrowserUrl(url);
            }
        });
    }

    private boolean handleBrowserNavigation(WebView view, Uri uri) {
        if (uri == null) {
            return false;
        }
        String scheme = uri.getScheme();
        if ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) {
            return false;
        }

        String fallback = firstNonEmpty(
                safeQueryParameter(uri, "params_url"),
                safeQueryParameter(uri, "url"),
                safeQueryParameter(uri, "redirect_url")
        );
        if (!TextUtils.isEmpty(fallback)) {
            String normalized = normalizeUrl(fallback);
            if (!TextUtils.isEmpty(normalized)) {
                view.loadUrl(normalized);
            }
        }
        return true;
    }

    private String safeQueryParameter(Uri uri, String key) {
        try {
            return uri.getQueryParameter(key);
        } catch (Exception ignored) {
            return "";
        }
    }

    private void openPlatform(SocialPlatform platform) {
        if (!ensureUsageApproved()) {
            return;
        }
        activePlatform = platform;
        updatePlatformHeader();
        hidePanels();
        if (appHeaderView != null) {
            appHeaderView.setVisibility(View.VISIBLE);
        }
        platformView.setText(platform.name + " " + ui("paneli"));
        browserPanel.setVisibility(View.VISIBLE);
        updateBrowserUrl(platform.homeUrl);
        webView.loadUrl(platform.homeUrl);
    }

    private void openPlatform(String label, String url) {
        SocialPlatform platform = platformByName(label);
        if (platform == null) {
            platform = new SocialPlatform(label, url, R.drawable.ic_link, Color.rgb(8, 127, 111));
        }
        openPlatform(platform);
    }

    private void showHome() {
        if (!ensureNoMandatoryUpdate()) {
            return;
        }
        if (!isSignedIn()) {
            showAuthPanel(false);
            return;
        }
        hidePanels();
        if (appHeaderView != null) {
            appHeaderView.setVisibility(View.VISIBLE);
        }
        if (homePanel != null) {
            homePanel.setVisibility(View.VISIBLE);
        }
    }

    private void showDownloader() {
        if (!ensureNoMandatoryUpdate()) {
            return;
        }
        if (!isSignedIn()) {
            showAuthPanel(false);
            return;
        }
        hidePanels();
        if (appHeaderView != null) {
            appHeaderView.setVisibility(View.VISIBLE);
        }
        downloadPanel.setVisibility(View.VISIBLE);
        platformView.setText(ui("Link modu"));
    }

    private void showAuthPanel(boolean loginMode) {
        forceDrawerClosed();
        hidePanels();
        if (appHeaderView != null) {
            appHeaderView.setVisibility(View.GONE);
        }
        if (authPanel != null) {
            authPanel.setVisibility(View.VISIBLE);
            renderAuthPanel(authPanel, loginMode);
        }
    }

    private void showDownloads() {
        if (!ensureNoMandatoryUpdate()) {
            return;
        }
        if (!isSignedIn()) {
            showAuthPanel(true);
            return;
        }
        hidePanels();
        if (appHeaderView != null) {
            appHeaderView.setVisibility(View.VISIBLE);
        }
        downloadsPanel.setVisibility(View.VISIBLE);
        refreshDownloads();
    }

    private void showSettings() {
        if (!ensureNoMandatoryUpdate()) {
            return;
        }
        hidePanels();
        if (appHeaderView != null) {
            appHeaderView.setVisibility(View.VISIBLE);
        }
        settingsPanel.setVisibility(View.VISIBLE);
        renderSettingsHome(settingsPanel);
    }

    private void showAbout() {
        if (!ensureNoMandatoryUpdate()) {
            return;
        }
        hidePanels();
        if (appHeaderView != null) {
            appHeaderView.setVisibility(View.VISIBLE);
        }
        aboutPanel.setVisibility(View.VISIBLE);
    }

    private void hidePanels() {
        if (authPanel != null) {
            authPanel.setVisibility(View.GONE);
        }
        if (homePanel != null) {
            homePanel.setVisibility(View.GONE);
        }
        if (downloadPanel != null) {
            downloadPanel.setVisibility(View.GONE);
        }
        if (browserPanel != null) {
            browserPanel.setVisibility(View.GONE);
        }
        if (downloadsPanel != null) {
            downloadsPanel.setVisibility(View.GONE);
        }
        if (settingsPanel != null) {
            settingsPanel.setVisibility(View.GONE);
        }
        if (aboutPanel != null) {
            aboutPanel.setVisibility(View.GONE);
        }
    }

    private boolean isSettingsSubPage() {
        return settingsPanel != null
                && settingsPanel.getChildCount() > 0
                && settingsPanel.getChildAt(0) instanceof LinearLayout;
    }

    private void updateBrowserUrl(String url) {
        String safeUrl = url == null ? "" : url;
        browserUrlView.setText(safeUrl);
        browserPageDownloadAvailable = detectPlatform(normalizeUrl(safeUrl)) != null;
        browserDownloadButton.setEnabled(browserPageDownloadAvailable && !busy && !downloading);
    }

    private void useCurrentBrowserPage() {
        String url = webView.getUrl();
        if (TextUtils.isEmpty(url)) {
            toast("Önce video sayfasını açın");
            return;
        }
        urlInput.setText(displayUrlForUi(url));
        urlInput.setSelection(urlInput.length());
        showDetectedPlatform(url);
        showDownloader();
        fetchFormatOptions(true);
    }

    private void refreshDownloads() {
        if (downloadsList == null) {
            return;
        }
        downloadsList.removeAllViews();
        displayedDownloads.clear();

        File root = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File jobs = root == null ? null : new File(root, "jobs");
        List<File> files = new ArrayList<>();
        if (jobs != null && jobs.isDirectory()) {
            collectCandidateFiles(jobs, files);
        }
        files.sort(Comparator.comparingLong(File::lastModified).reversed());
        pruneHiddenDownloadKeys(files);
        Set<String> hiddenDownloadKeys = hiddenDownloadKeys();

        String query = downloadSearchInput == null ? "" : downloadSearchInput.getText().toString().trim();
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
        for (File file : files) {
            if (!hiddenDownloadKeys.contains(downloadKey(file)) && matchesDownloadSearch(file, formatter, query)) {
                displayedDownloads.add(file);
            }
        }
        pruneDownloadSelection(displayedDownloads);
        updateDownloadSelectionBar();

        if (displayedDownloads.isEmpty()) {
            String message = files.isEmpty()
                    ? "Henüz indirilen dosya yok."
                    : (TextUtils.isEmpty(query) ? "İndirme listesi temiz. Dosyalar klasörde duruyor." : "Aramaya uygun dosya bulunamadı.");
            TextView empty = textView(message, 13, Color.rgb(96, 112, 106), false);
            downloadsList.addView(empty);
            return;
        }

        for (File file : displayedDownloads) {
            downloadsList.addView(downloadCard(file, formatter));
        }
    }

    private View downloadCard(File file, DateFormat formatter) {
        SocialPlatform platform = platformByDownloadedFile(file);
        int accent = platform == null ? Color.rgb(8, 127, 111) : platform.accentColor;
        String platformName = platform == null ? "Dosya" : platform.name;
        String optionLabel = downloadOptionByDownloadedFile(file);
        boolean selected = selectedDownloadPaths.contains(downloadKey(file));
        AppThemeOption theme = currentTheme();
        int titleColor = selected ? readableOn(accent) : accent;
        int detailColor = selected ? readableOn(accent) : theme.mutedColor;

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(12), dp(12), dp(12), dp(12));
        card.setBackground(rounded(selected ? accent : softAccent(accent), 8, accent));
        card.setOnLongClickListener(v -> {
            enterDownloadSelection(file);
            return true;
        });
        card.setOnClickListener(v -> {
            if (downloadSelectionMode) {
                toggleDownloadSelection(file);
            }
        });

        LinearLayout headerRow = new LinearLayout(this);
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setGravity(Gravity.CENTER_VERTICAL);
        card.addView(headerRow, matchWrap());

        ImageView platformIcon = new ImageView(this);
        platformIcon.setImageResource(platform == null ? R.drawable.ic_downloads : platform.iconRes);
        if (platform == null) {
            platformIcon.setColorFilter(theme.mutedColor);
        }
        platformIcon.setPadding(dp(4), dp(4), dp(4), dp(4));
        platformIcon.setBackground(rounded(theme.surfaceAltColor, 8, accent));
        headerRow.addView(platformIcon, new LinearLayout.LayoutParams(dp(46), dp(46)));

        LinearLayout textBlock = new LinearLayout(this);
        textBlock.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        textParams.leftMargin = dp(12);
        headerRow.addView(textBlock, textParams);

        TextView name = textView(file.getName(), 14, titleColor, true);
        name.setSingleLine(false);
        textBlock.addView(name);

        String meta = platformName
                + (TextUtils.isEmpty(optionLabel) ? "" : "  |  " + optionLabel)
                + "  |  " + readableSize(file.length())
                + "  |  " + formatter.format(new Date(file.lastModified()));
        TextView details = textView(meta, 12, detailColor, false);
        details.setPadding(0, dp(4), 0, 0);
        textBlock.addView(details);

        if (downloadSelectionMode) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setChecked(selected);
            checkBox.setOnClickListener(v -> toggleDownloadSelection(file));
            headerRow.addView(checkBox, new LinearLayout.LayoutParams(dp(46), dp(46)));
        } else {
            LinearLayout actions = new LinearLayout(this);
            actions.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams actionParams = matchWrap();
            actionParams.topMargin = dp(10);
            card.addView(actions, actionParams);

            Button open = secondaryButton("Aç");
            open.setOnClickListener(v -> openFile(file));
            actions.addView(open, rowWeight());

            Button share = secondaryButton("Paylaş");
            share.setOnClickListener(v -> shareFile(file));
            LinearLayout.LayoutParams shareParams = rowWeight();
            shareParams.leftMargin = dp(8);
            actions.addView(share, shareParams);
        }

        LinearLayout.LayoutParams params = matchWrap();
        params.topMargin = dp(8);
        card.setLayoutParams(params);
        return card;
    }

    private boolean matchesDownloadSearch(File file, DateFormat formatter, String query) {
        if (TextUtils.isEmpty(query)) {
            return true;
        }
        SocialPlatform platform = platformByDownloadedFile(file);
        String platformName = platform == null ? "Dosya" : platform.name;
        String optionLabel = downloadOptionByDownloadedFile(file);
        String haystack = file.getName()
                + " " + platformName
                + " " + optionLabel
                + " " + readableSize(file.length())
                + " " + formatter.format(new Date(file.lastModified()));
        return haystack.toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()));
    }

    private void enterDownloadSelection(File file) {
        downloadSelectionMode = true;
        selectedDownloadPaths.add(downloadKey(file));
        updateDownloadSelectionBar();
        refreshDownloads();
    }

    private void toggleDownloadSelection(File file) {
        String key = downloadKey(file);
        if (selectedDownloadPaths.contains(key)) {
            selectedDownloadPaths.remove(key);
        } else {
            selectedDownloadPaths.add(key);
        }
        updateDownloadSelectionBar();
        refreshDownloads();
    }

    private void toggleSelectAllVisibleDownloads() {
        if (displayedDownloads.isEmpty()) {
            toast("Seçilecek dosya yok");
            return;
        }
        downloadSelectionMode = true;
        int visibleSelected = selectedVisibleDownloadCount();
        if (visibleSelected == displayedDownloads.size()) {
            for (File file : displayedDownloads) {
                selectedDownloadPaths.remove(downloadKey(file));
            }
        } else {
            for (File file : displayedDownloads) {
                selectedDownloadPaths.add(downloadKey(file));
            }
        }
        updateDownloadSelectionBar();
        refreshDownloads();
    }

    private void clearDownloadSelection(boolean refresh) {
        selectedDownloadPaths.clear();
        downloadSelectionMode = false;
        updateDownloadSelectionBar();
        if (refresh) {
            refreshDownloads();
        }
    }

    private void updateDownloadSelectionBar() {
        if (downloadsSelectionBar == null || downloadSelectionStatus == null) {
            return;
        }
        downloadsSelectionBar.setVisibility(downloadSelectionMode ? View.VISIBLE : View.GONE);
        int visibleSelected = selectedVisibleDownloadCount();
        downloadSelectionStatus.setText(ui("Seçilen: " + visibleSelected + " / " + displayedDownloads.size()));
        if (deleteSelectedDownloadsButton != null) {
            deleteSelectedDownloadsButton.setEnabled(visibleSelected > 0);
            deleteSelectedDownloadsButton.setText(ui(visibleSelected > 0 ? "Listeden kaldır (" + visibleSelected + ")" : "Listeden kaldır"));
        }
        if (selectAllDownloadsButton != null) {
            boolean allVisibleSelected = !displayedDownloads.isEmpty() && visibleSelected == displayedDownloads.size();
            selectAllDownloadsButton.setText(ui(allVisibleSelected ? "Seçimi kaldır" : "Tümünü seç"));
            selectAllDownloadsButton.setEnabled(!displayedDownloads.isEmpty());
        }
    }

    private int selectedVisibleDownloadCount() {
        int count = 0;
        for (File file : displayedDownloads) {
            if (selectedDownloadPaths.contains(downloadKey(file))) {
                count++;
            }
        }
        return count;
    }

    private void pruneDownloadSelection(List<File> validFiles) {
        if (selectedDownloadPaths.isEmpty()) {
            return;
        }
        Set<String> validPaths = new HashSet<>();
        for (File file : validFiles) {
            validPaths.add(downloadKey(file));
        }
        selectedDownloadPaths.retainAll(validPaths);
        if (selectedDownloadPaths.isEmpty() && !downloadSelectionMode) {
            updateDownloadSelectionBar();
        }
    }

    private void confirmRemoveSelectedDownloadsFromApp() {
        int count = selectedVisibleDownloadCount();
        if (count <= 0) {
            toast("Önce dosya seçin");
            return;
        }
        confirm(
                "Seçilenleri listeden kaldır",
                count + " kayıt uygulama listesinden kaldırılsın mı? Dosyalar klasörden silinmez.",
                this::removeSelectedDownloadsFromApp
        );
    }

    private void confirmClearDownloadsList() {
        List<File> visibleFiles = visibleDownloadFilesWithoutSearch();
        if (visibleFiles.isEmpty()) {
            toast("Temizlenecek kayıt yok");
            return;
        }
        confirm(
                "İndirme listesini temizle",
                visibleFiles.size() + " kayıt uygulama listesinden kaldırılsın mı? Dosyalar klasörden silinmez.",
                () -> clearDownloadsListFromApp(visibleFiles)
        );
    }

    private void clearDownloadsListFromApp(List<File> files) {
        hideDownloadsFromApp(files);
        for (File file : files) {
            if (lastFile != null && sameFile(file, lastFile)) {
                lastFile = null;
                lastUri = null;
                setFileActionsEnabled(false);
                hideLastDownloadSummary();
            }
        }
        clearDownloadSelection(false);
        refreshDownloads();
        toast(files.size() + " kayıt listeden kaldırıldı");
    }

    private void removeSelectedDownloadsFromApp() {
        Set<String> selected = new HashSet<>(selectedDownloadPaths);
        List<File> filesToHide = new ArrayList<>();
        int removed = 0;

        for (File file : new ArrayList<>(displayedDownloads)) {
            if (!selected.contains(downloadKey(file))) {
                continue;
            }
            filesToHide.add(file);
            removed++;
            if (lastFile != null && sameFile(file, lastFile)) {
                lastFile = null;
                lastUri = null;
                setFileActionsEnabled(false);
                hideLastDownloadSummary();
            }
        }

        hideDownloadsFromApp(filesToHide);
        clearDownloadSelection(false);
        refreshDownloads();
        toast(removed + " kayıt listeden kaldırıldı");
    }

    private List<File> visibleDownloadFilesWithoutSearch() {
        File root = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File jobs = root == null ? null : new File(root, "jobs");
        List<File> files = new ArrayList<>();
        if (jobs != null && jobs.isDirectory()) {
            collectCandidateFiles(jobs, files);
        }
        files.sort(Comparator.comparingLong(File::lastModified).reversed());
        pruneHiddenDownloadKeys(files);
        Set<String> hidden = hiddenDownloadKeys();
        List<File> visible = new ArrayList<>();
        for (File file : files) {
            if (!hidden.contains(downloadKey(file))) {
                visible.add(file);
            }
        }
        return visible;
    }

    private void hideDownloadsFromApp(List<File> files) {
        if (files == null || files.isEmpty()) {
            return;
        }
        Set<String> hidden = hiddenDownloadKeys();
        boolean changed = false;
        for (File file : files) {
            String key = downloadKey(file);
            if (!TextUtils.isEmpty(key) && hidden.add(key)) {
                changed = true;
            }
        }
        if (changed) {
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    .edit()
                    .putStringSet(PREF_HIDDEN_DOWNLOAD_KEYS, hidden)
                    .apply();
        }
    }

    private Set<String> hiddenDownloadKeys() {
        Set<String> stored = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getStringSet(PREF_HIDDEN_DOWNLOAD_KEYS, Collections.emptySet());
        return new HashSet<>(stored);
    }

    private void pruneHiddenDownloadKeys(List<File> existingFiles) {
        Set<String> hidden = hiddenDownloadKeys();
        if (hidden.isEmpty()) {
            return;
        }
        Set<String> existing = new HashSet<>();
        for (File file : existingFiles) {
            existing.add(downloadKey(file));
        }
        if (hidden.retainAll(existing)) {
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    .edit()
                    .putStringSet(PREF_HIDDEN_DOWNLOAD_KEYS, hidden)
                    .apply();
        }
    }

    private void cleanupJobDirectory(File jobDirectory) {
        if (jobDirectory == null || !jobDirectory.isDirectory()) {
            return;
        }
        List<File> remainingDownloads = new ArrayList<>();
        collectCandidateFiles(jobDirectory, remainingDownloads);
        if (!remainingDownloads.isEmpty()) {
            return;
        }
        deleteChildren(jobDirectory);
        if (!jobDirectory.delete()) {
            jobDirectory.deleteOnExit();
        }
    }

    private File jobDirectoryFor(File file) {
        File root = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File jobsRoot = root == null ? null : new File(root, "jobs");
        if (file == null || jobsRoot == null) {
            return null;
        }
        File current = file.getParentFile();
        while (current != null) {
            File parent = current.getParentFile();
            if (parent != null && sameFile(parent, jobsRoot)) {
                return current;
            }
            if (sameFile(current, jobsRoot)) {
                return null;
            }
            current = parent;
        }
        return file.getParentFile();
    }

    private String downloadKey(File file) {
        if (file == null) {
            return "";
        }
        try {
            return file.getCanonicalPath();
        } catch (IOException ignored) {
            return file.getAbsolutePath();
        }
    }

    private static boolean sameFile(File first, File second) {
        if (first == null || second == null) {
            return false;
        }
        try {
            return first.getCanonicalFile().equals(second.getCanonicalFile());
        } catch (IOException ignored) {
            return first.getAbsolutePath().equals(second.getAbsolutePath());
        }
    }

    private void writeJobMetadata(File jobDir, String platformName, String url, DownloadOption option) {
        if (jobDir == null) {
            return;
        }
        Properties properties = new Properties();
        properties.setProperty("platform", platformName == null ? "" : platformName);
        properties.setProperty("url", url == null ? "" : url);
        properties.setProperty("option", option == null ? "" : option.label);
        try (OutputStream output = new FileOutputStream(new File(jobDir, JOB_METADATA_FILE))) {
            properties.store(output, "MetaFold download metadata");
        } catch (IOException ignored) {
            // Metadata improves the downloads UI but should never block a download.
        }
    }

    private SocialPlatform platformByDownloadedFile(File file) {
        Properties properties = downloadMetadata(file);
        if (properties == null) {
            return null;
        }
        String platformName = properties.getProperty("platform", "");
        SocialPlatform platform = platformByDetectedName(platformName);
        if (platform != null) {
            return platform;
        }
        return platformByDetectedName(detectPlatform(properties.getProperty("url", "")));
    }

    private String downloadOptionByDownloadedFile(File file) {
        Properties properties = downloadMetadata(file);
        return properties == null ? "" : properties.getProperty("option", "");
    }

    private Properties downloadMetadata(File file) {
        File metadataFile = metadataFileForDownloadedFile(file);
        if (metadataFile == null || !metadataFile.isFile()) {
            return null;
        }
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(metadataFile)) {
            properties.load(input);
            return properties;
        } catch (IOException ignored) {
            return null;
        }
    }

    private File metadataFileForDownloadedFile(File file) {
        if (file == null) {
            return null;
        }
        File directory = file.getParentFile();
        if (directory == null) {
            return null;
        }
        return new File(directory, JOB_METADATA_FILE);
    }

    private void setLanguage(String language) {
        selectedLanguage = language;
        putString(PREF_LANGUAGE, language);
        if (settingsPanel != null && settingsPanel.getVisibility() == View.VISIBLE) {
            renderSettingsHome(settingsPanel);
        }
        toast("Dil: " + languageName(language));
    }

    private void refreshLanguageStatus() {
        if (settingsPanel == null) {
            return;
        }
        View status = settingsPanel.findViewWithTag("language_status");
        if (status instanceof TextView) {
            ((TextView) status).setText(ui("Aktif dil: " + languageName(selectedLanguage)));
        }
    }

    private void openWebsite(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception error) {
            toast("Site açılamadı");
        }
    }

    private void openWhatsAppLicensePurchase(String rawEmail) {
        String email = normalizeEmail(firstNonEmpty(rawEmail, currentAuthEmail(), getString(PREF_LICENSE_EMAIL, "")));
        StringBuilder message = new StringBuilder();
        message.append("Merhaba, MetaFold Downloader lisansı satın almak istiyorum.");
        if (!TextUtils.isEmpty(email)) {
            message.append("\nE-posta: ").append(email);
        }
        message.append("\nCihaz: ").append(Build.MANUFACTURER).append(" ").append(Build.MODEL);
        message.append("\nCihaz ID: ").append(shortDeviceId());
        message.append("\nSürüm: ").append(currentAppVersion());
        try {
            String encoded = URLEncoder.encode(message.toString(), "UTF-8");
            String url = "https://wa.me/" + LICENSE_WHATSAPP_NUMBER + "?text=" + encoded;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception error) {
            toast("WhatsApp açılamadı");
        }
    }

    private void chooseDownloadFolder(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        try {
            startActivityForResult(intent, requestCode);
        } catch (Exception error) {
            toast("Klasör seçici açılamadı");
        }
    }

    private String folderLabel(String prefKey) {
        String stored = getString(prefKey, "");
        if (TextUtils.isEmpty(stored)) {
            return "Downloads/" + DOWNLOAD_FOLDER;
        }
        try {
            DocumentFile folder = DocumentFile.fromTreeUri(this, Uri.parse(stored));
            String name = folder == null ? "" : folder.getName();
            return TextUtils.isEmpty(name) ? "Seçili klasör" : name;
        } catch (Exception ignored) {
            return "Seçili klasör";
        }
    }

    private void confirm(String title, String message, Runnable action) {
        new AlertDialog.Builder(this)
                .setTitle(ui(title))
                .setMessage(ui(message))
                .setPositiveButton(ui("Evet"), (dialog, which) -> action.run())
                .setNegativeButton(ui("Vazgeç"), null)
                .show();
    }

    private void showPlatformLoginRequiredDialog(String platform, String originalUrl) {
        SocialPlatform socialPlatform = platformByDetectedName(platform);
        if (socialPlatform == null) {
            return;
        }
        String openLabel = isEnglish() ? "Open " + socialPlatform.name : socialPlatform.name + "'a git";
        new AlertDialog.Builder(this)
                .setTitle(ui("Oturumla tekrar dene"))
                .setMessage(ui("Girişsiz indirme başarısız oldu. Bu içerik gizli, kısıtlı veya oturum çerezi istiyor olabilir. İsterseniz uygulama içindeki platform ekranında giriş yapıp videoyu oradan indirebilirsiniz."))
                .setPositiveButton(openLabel, (dialog, which) -> {
                    urlInput.setText(displayUrlForUi(originalUrl));
                    urlInput.setSelection(urlInput.length());
                    openPlatform(socialPlatform);
                })
                .setNegativeButton(ui("Vazgeç"), null)
                .show();
    }

    private void maybeCheckAppUpdateOnStartup() {
        checkAppUpdate(true);
    }

    private void checkAppUpdate(boolean silent) {
        if (mandatoryUpdateRequired) {
            showMandatoryUpdateDialog(storedMandatoryUpdateInfo());
        }
        if (!silent) {
            setBusy(true, "Uygulama güncellemesi denetleniyor...");
        }
        executor.execute(() -> {
            try {
                AppUpdateInfo updateInfo = fetchLatestAppUpdate();
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit()
                        .putLong(PREF_APP_UPDATE_LAST_CHECK, System.currentTimeMillis())
                        .apply();
                mainHandler.post(() -> {
                    if (!silent) {
                        setBusy(false, null);
                    }
                    handleAppUpdateInfo(updateInfo, silent);
                });
            } catch (Exception error) {
                mainHandler.post(() -> {
                    if (mandatoryUpdateRequired) {
                        showMandatoryUpdateDialog(storedMandatoryUpdateInfo());
                    }
                    if (!silent) {
                        setBusy(false, null);
                        setStatus("Uygulama güncellemesi denetlenemedi.", false);
                        outputView.setText(cleanError(error));
                    }
                });
            }
        });
    }

    private AppUpdateInfo fetchLatestAppUpdate() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(GITHUB_LATEST_RELEASE_API).openConnection();
        connection.setConnectTimeout(12000);
        connection.setReadTimeout(12000);
        connection.setRequestProperty("Accept", "application/vnd.github+json");
        connection.setRequestProperty("User-Agent", "MetaFoldDownloader/" + currentAppVersion());
        int code = connection.getResponseCode();
        String body = readHttpBody(code >= 200 && code < 300 ? connection.getInputStream() : connection.getErrorStream());
        if (code == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new IOException("GitHub release okunamadı. Repo private ise uygulama buraya tokensız erişemez; release veya update JSON public olmalı.");
        }
        if (code < 200 || code >= 300) {
            throw new IOException("GitHub release okunamadı. HTTP " + code + ": " + body);
        }

        JSONObject json = new JSONObject(body);
        String tagName = json.optString("tag_name", "");
        String latestVersion = normalizeVersionTag(tagName);
        String releaseName = json.optString("name", tagName);
        String releaseBody = json.optString("body", "");
        String htmlUrl = json.optString("html_url", GITHUB_RELEASES_URL);
        String apkUrl = "";
        JSONArray assets = json.optJSONArray("assets");
        if (assets != null) {
            for (int i = 0; i < assets.length(); i++) {
                JSONObject asset = assets.optJSONObject(i);
                if (asset == null) {
                    continue;
                }
                String assetName = asset.optString("name", "").toLowerCase(Locale.US);
                String downloadUrl = asset.optString("browser_download_url", "");
                if (!assetName.endsWith(".apk") || TextUtils.isEmpty(downloadUrl)) {
                    continue;
                }
                if (TextUtils.isEmpty(apkUrl)
                        || assetName.contains("universal")
                        || (assetName.contains("arm64") && !apkUrl.toLowerCase(Locale.US).contains("universal"))) {
                    apkUrl = downloadUrl;
                }
            }
        }

        String currentVersion = currentAppVersion();
        boolean newer = compareVersions(latestVersion, currentVersion) > 0;
        boolean mandatory = isMandatoryRelease(releaseName, releaseBody);
        return new AppUpdateInfo(releaseName, tagName, latestVersion, currentVersion, htmlUrl, apkUrl, newer, mandatory);
    }

    private void handleAppUpdateInfo(AppUpdateInfo updateInfo, boolean silent) {
        if (updateInfo.newer) {
            if (updateInfo.mandatory) {
                storeMandatoryUpdate(updateInfo);
                showMandatoryUpdateDialog(updateInfo);
                return;
            }
            clearMandatoryUpdateLock();
            if (!silent || !isUpdateReminderSnoozed(updateInfo)) {
                showAppUpdateFoundDialog(updateInfo);
            }
            return;
        }
        clearMandatoryUpdateLock();
        if (!silent) {
            setStatus("Uygulama güncel.", true);
            new AlertDialog.Builder(this)
                    .setTitle(ui("Uygulama güncel"))
                    .setMessage(ui("Kurulu sürüm") + ": " + updateInfo.currentVersion + "\n" +
                            ui("GitHub sürümü") + ": " + updateInfo.latestLabel())
                    .setPositiveButton(ui("Tamam"), null)
                    .show();
        }
    }

    private void restoreMandatoryUpdateLock() {
        AppUpdateInfo stored = storedMandatoryUpdateInfo();
        mandatoryUpdateRequired = stored != null && stored.newer;
        if (mandatoryUpdateRequired) {
            mainHandler.post(() -> showMandatoryUpdateDialog(stored));
        } else {
            clearMandatoryUpdateLock();
        }
    }

    private AppUpdateInfo storedMandatoryUpdateInfo() {
        String latestVersion = getString(PREF_MANDATORY_UPDATE_VERSION, "");
        if (TextUtils.isEmpty(latestVersion) || compareVersions(latestVersion, currentAppVersion()) <= 0) {
            return null;
        }
        String releaseName = getString(PREF_MANDATORY_UPDATE_RELEASE_NAME, "MetaFold Downloader " + latestVersion);
        String htmlUrl = getString(PREF_MANDATORY_UPDATE_HTML_URL, GITHUB_RELEASES_URL);
        String apkUrl = getString(PREF_MANDATORY_UPDATE_APK_URL, "");
        return new AppUpdateInfo(releaseName, latestVersion, latestVersion, currentAppVersion(), htmlUrl, apkUrl, true, true);
    }

    private void storeMandatoryUpdate(AppUpdateInfo updateInfo) {
        mandatoryUpdateRequired = true;
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(PREF_MANDATORY_UPDATE_VERSION, updateInfo.latestVersion)
                .putString(PREF_MANDATORY_UPDATE_RELEASE_NAME, updateInfo.releaseName)
                .putString(PREF_MANDATORY_UPDATE_HTML_URL, updateInfo.htmlUrl)
                .putString(PREF_MANDATORY_UPDATE_APK_URL, updateInfo.apkUrl)
                .apply();
    }

    private void clearMandatoryUpdateLock() {
        mandatoryUpdateRequired = false;
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .remove(PREF_MANDATORY_UPDATE_VERSION)
                .remove(PREF_MANDATORY_UPDATE_RELEASE_NAME)
                .remove(PREF_MANDATORY_UPDATE_HTML_URL)
                .remove(PREF_MANDATORY_UPDATE_APK_URL)
                .apply();
    }

    private boolean ensureNoMandatoryUpdate() {
        if (!mandatoryUpdateRequired) {
            return true;
        }
        showMandatoryUpdateDialog(storedMandatoryUpdateInfo());
        return false;
    }

    private void showAppUpdateDialog(AppUpdateInfo updateInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(ui("Yeni sürüm hazır"))
                .setMessage(ui("Kurulu sürüm") + ": " + updateInfo.currentVersion + "\n" +
                        ui("Yeni sürüm") + ": " + updateInfo.latestLabel() + "\n\n" +
                        ui("Güncelleme uygulama içinde indirilecek. Kurulum ekranında Android onayı gereklidir."))
                .setNegativeButton(ui("Vazgeç"), null);
        if (!TextUtils.isEmpty(updateInfo.apkUrl)) {
            builder.setPositiveButton(ui("İndir ve kur"), (dialog, which) -> openUpdateTarget(updateInfo));
            builder.setNeutralButton(ui("Release sayfasını aç"), (dialog, which) -> openWebsite(updateInfo.htmlUrl));
        } else {
            builder.setPositiveButton(ui("Release sayfasını aç"), (dialog, which) -> openWebsite(updateInfo.htmlUrl));
        }
        builder.show();
    }

    private void showAppUpdateFoundDialog(AppUpdateInfo updateInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(ui("Güncelleme bulundu"))
                .setMessage(ui("Kurulu sürüm") + ": " + updateInfo.currentVersion + "\n" +
                        ui("Yeni sürüm") + ": " + updateInfo.latestLabel() + "\n\n" +
                        ui("Güncelleme uygulama içinde indirilecek. Kurulum ekranında Android onayı gereklidir."))
                .setNegativeButton(ui("Daha sonra hatırlat"), (dialog, which) -> snoozeAppUpdate(updateInfo));
        if (!TextUtils.isEmpty(updateInfo.apkUrl)) {
            builder.setPositiveButton(ui("İndir ve kur"), (dialog, which) -> openUpdateTarget(updateInfo));
            builder.setNeutralButton(ui("Release sayfasını aç"), (dialog, which) -> openWebsite(updateInfo.htmlUrl));
        } else {
            builder.setPositiveButton(ui("Release sayfasını aç"), (dialog, which) -> openWebsite(updateInfo.htmlUrl));
        }
        builder.show();
    }

    private static boolean isMandatoryRelease(String releaseName, String releaseBody) {
        String text = (releaseName + "\n" + releaseBody).toLowerCase(Locale.US);
        return text.contains("[mandatory]")
                || text.contains("[force-update]")
                || text.contains("mandatory update")
                || text.contains("zorunlu güncelleme");
    }

    private void openUpdateTarget(AppUpdateInfo updateInfo) {
        if (updateInfo == null || TextUtils.isEmpty(updateInfo.apkUrl)) {
            openWebsite(updateInfo == null ? GITHUB_RELEASES_URL : updateInfo.htmlUrl);
            return;
        }
        downloadAndInstallUpdate(updateInfo);
    }

    private void downloadAndInstallUpdate(AppUpdateInfo updateInfo) {
        showUpdateDownloadDialog(updateInfo);
        executor.execute(() -> {
            try {
                File apk = downloadUpdateApk(updateInfo);
                mainHandler.post(() -> {
                    dismissUpdateDownloadDialog();
                    setStatus("Güncelleme indirildi. Kurulum ekranı açılıyor...", true);
                    installDownloadedUpdate(apk);
                });
            } catch (Exception error) {
                mainHandler.post(() -> {
                    dismissUpdateDownloadDialog();
                    setStatus("Güncelleme indirilemedi.", false);
                    new AlertDialog.Builder(this)
                            .setTitle(ui("Güncelleme indirilemedi"))
                            .setMessage(cleanError(error))
                            .setPositiveButton(ui("Release sayfasını aç"), (dialog, which) -> openWebsite(updateInfo.htmlUrl))
                            .setNegativeButton(ui("Vazgeç"), null)
                            .show();
                });
            }
        });
    }

    private File downloadUpdateApk(AppUpdateInfo updateInfo) throws IOException {
        File updateDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "updates");
        if (!updateDir.exists() && !updateDir.mkdirs()) {
            throw new IOException("Güncelleme klasörü oluşturulamadı: " + updateDir.getAbsolutePath());
        }
        deleteOldUpdateApks(updateDir);

        String version = normalizeVersionTag(firstNonEmpty(updateInfo.latestVersion, updateInfo.tagName, "latest"));
        File output = new File(updateDir, "MetaFold-Downloader-v" + version + ".apk");
        if (output.exists() && !output.delete()) {
            output.deleteOnExit();
        }

        HttpURLConnection connection = openUpdateDownloadConnection(updateInfo.apkUrl);
        int code = connection.getResponseCode();
        if (code < 200 || code >= 300) {
            String body = readHttpBody(connection.getErrorStream());
            throw new IOException("APK indirilemedi. HTTP " + code + ": " + body);
        }

        long total = connection.getContentLengthLong();
        long downloaded = 0L;
        int lastPercent = -1;
        byte[] buffer = new byte[1024 * 64];
        try (InputStream input = connection.getInputStream();
             OutputStream outputStream = new FileOutputStream(output)) {
            int read;
            while ((read = input.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
                downloaded += read;
                if (total > 0L) {
                    int percent = Math.min(100, (int) ((downloaded * 100L) / total));
                    if (percent != lastPercent) {
                        lastPercent = percent;
                        long currentDownloaded = downloaded;
                        mainHandler.post(() -> updateUpdateDownloadProgress(percent, currentDownloaded, total));
                    }
                }
            }
        } finally {
            connection.disconnect();
        }

        if (!output.exists() || output.length() <= 0L) {
            throw new IOException("İndirilen APK boş görünüyor.");
        }
        mainHandler.post(() -> updateUpdateDownloadProgress(100, output.length(), Math.max(output.length(), total)));
        return output;
    }

    private HttpURLConnection openUpdateDownloadConnection(String url) throws IOException {
        String currentUrl = url;
        for (int redirect = 0; redirect < 6; redirect++) {
            HttpURLConnection connection = (HttpURLConnection) new URL(currentUrl).openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(45000);
            connection.setRequestProperty("User-Agent", "MetaFoldDownloader/" + currentAppVersion());
            int code = connection.getResponseCode();
            if (code >= 300 && code < 400) {
                String location = connection.getHeaderField("Location");
                connection.disconnect();
                if (TextUtils.isEmpty(location)) {
                    throw new IOException("Güncelleme yönlendirmesi boş döndü.");
                }
                currentUrl = new URL(new URL(currentUrl), location).toString();
                continue;
            }
            return connection;
        }
        throw new IOException("Güncelleme indirme yönlendirmesi çok fazla tekrarlandı.");
    }

    private void deleteOldUpdateApks(File updateDir) {
        File[] files = updateDir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase(Locale.US).endsWith(".apk") && !file.delete()) {
                file.deleteOnExit();
            }
        }
    }

    private void installDownloadedUpdate(File apk) {
        if (apk == null || !apk.exists()) {
            setStatus("Güncelleme APK dosyası bulunamadı.", false);
            return;
        }
        if (!canRequestPackageInstalls()) {
            pendingUpdateApk = apk;
            showInstallPermissionDialog();
            return;
        }
        launchPackageInstaller(apk);
    }

    private boolean canRequestPackageInstalls() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.O || getPackageManager().canRequestPackageInstalls();
    }

    private void showInstallPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle(ui("Kurulum izni gerekli"))
                .setMessage(ui("Android bu uygulamanın APK yükleyebilmesi için izin istiyor. İzni verdikten sonra kurulum ekranı otomatik açılacak."))
                .setPositiveButton(ui("Ayarları aç"), (dialog, which) -> openInstallPermissionSettings())
                .setNegativeButton(ui("Vazgeç"), null)
                .show();
    }

    private void openInstallPermissionSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception error) {
            try {
                startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
            } catch (Exception ignored) {
                setStatus("Kurulum izni ekranı açılamadı.", false);
            }
        }
    }

    private void launchPackageInstaller(File apk) {
        try {
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".files", apk);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            setStatus("Kurulum ekranı açıldı. Devam etmek için Android onayını verin.", true);
        } catch (Exception error) {
            setStatus("Kurulum ekranı açılamadı.", false);
            new AlertDialog.Builder(this)
                    .setTitle(ui("Kurulum ekranı açılamadı"))
                    .setMessage(cleanError(error))
                    .setPositiveButton(ui("Release sayfasını aç"), (dialog, which) -> openWebsite(GITHUB_RELEASES_URL))
                    .setNegativeButton(ui("Vazgeç"), null)
                    .show();
        }
    }

    private void showUpdateDownloadDialog(AppUpdateInfo updateInfo) {
        dismissUpdateDownloadDialog();
        AppThemeOption theme = currentTheme();
        String version = updateInfo == null ? "" : updateInfo.latestLabel();

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        AnimatedUpdateCardView card = new AnimatedUpdateCardView(this, theme);
        card.setPadding(dp(20), dp(20), dp(20), dp(18));
        card.setAlpha(0f);
        card.setScaleX(0.96f);
        card.setScaleY(0.96f);

        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        card.addView(panel, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        ));

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        panel.addView(header, matchWrap());

        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.app_icon);
        icon.setPadding(dp(4), dp(4), dp(4), dp(4));
        icon.setBackground(rounded(blend(theme.surfaceAltColor, theme.accentColor, 0.18f), 8, theme.accentColor));
        header.addView(icon, new LinearLayout.LayoutParams(dp(52), dp(52)));

        LinearLayout titleBlock = new LinearLayout(this);
        titleBlock.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        titleParams.leftMargin = dp(12);
        header.addView(titleBlock, titleParams);

        TextView eyebrow = textView("MetaFold Downloader", 12, theme.accentColor, true);
        titleBlock.addView(eyebrow);

        TextView title = textView("Güncelleme indiriliyor", 20, theme.textColor, true);
        title.setPadding(0, dp(2), 0, 0);
        titleBlock.addView(title);

        TextView subtitle = textView(TextUtils.isEmpty(version) ? "APK hazırlanıyor" : ui("Yeni sürüm") + ": " + version, 12, theme.mutedColor, false);
        subtitle.setPadding(0, dp(4), 0, 0);
        titleBlock.addView(subtitle);

        updateDownloadPercent = textView("%0", 16, readableOn(theme.accentColor), true);
        updateDownloadPercent.setGravity(Gravity.CENTER);
        updateDownloadPercent.setBackground(rounded(theme.accentColor, 18, theme.accentColor));
        header.addView(updateDownloadPercent, new LinearLayout.LayoutParams(dp(58), dp(36)));

        updateDownloadStatus = textView("Bağlantı kuruluyor...", 14, theme.textColor, true);
        updateDownloadStatus.setPadding(0, dp(18), 0, 0);
        panel.addView(updateDownloadStatus, matchWrap());

        updateDownloadBytes = textView("Dosya boyutu hesaplanıyor", 12, theme.mutedColor, false);
        updateDownloadBytes.setPadding(0, dp(4), 0, 0);
        panel.addView(updateDownloadBytes, matchWrap());

        updateDownloadProgress = new AnimatedUpdateProgressView(this, theme);
        LinearLayout.LayoutParams progressParams = matchWrap();
        progressParams.topMargin = dp(16);
        progressParams.height = dp(16);
        panel.addView(updateDownloadProgress, progressParams);

        TextView footnote = textView("İndirme tamamlanınca kurulum ekranı açılacak.", 12, theme.mutedColor, false);
        footnote.setPadding(0, dp(12), 0, 0);
        panel.addView(footnote, matchWrap());

        dialog.setContentView(card);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        updateDownloadDialog = dialog;
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = 0.76f;
            window.setAttributes(params);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            int width = Math.min(getResources().getDisplayMetrics().widthPixels - dp(40), dp(430));
            window.setLayout(Math.max(dp(280), width), WindowManager.LayoutParams.WRAP_CONTENT);
        }
        card.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(220)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    private void updateUpdateDownloadProgress(int percent, long downloaded, long total) {
        int safePercent = Math.max(0, Math.min(100, percent));
        if (updateDownloadProgress != null) {
            updateDownloadProgress.setProgressPercent(safePercent, total <= 0L);
        }
        if (updateDownloadPercent != null) {
            updateDownloadPercent.setText("%" + safePercent);
        }
        if (updateDownloadStatus != null) {
            updateDownloadStatus.setText(safePercent >= 100 ? ui("Güncelleme hazırlandı") : ui("Güncelleme indiriliyor..."));
        }
        if (updateDownloadBytes != null) {
            String sizeText = total > 0L
                    ? formatBytes(downloaded) + " / " + formatBytes(total)
                    : formatBytes(downloaded);
            updateDownloadBytes.setText(sizeText);
        }
    }

    private void dismissUpdateDownloadDialog() {
        if (updateDownloadDialog != null && updateDownloadDialog.isShowing()) {
            updateDownloadDialog.dismiss();
        }
        updateDownloadDialog = null;
        updateDownloadProgress = null;
        updateDownloadStatus = null;
        updateDownloadPercent = null;
        updateDownloadBytes = null;
    }

    private void snoozeAppUpdate(AppUpdateInfo updateInfo) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(PREF_APP_UPDATE_REMIND_VERSION, updateInfo.latestVersion)
                .putLong(PREF_APP_UPDATE_REMIND_AFTER, System.currentTimeMillis() + UPDATE_REMIND_LATER_MS)
                .apply();
        toast("Daha sonra hatırlatılacak");
    }

    private boolean isUpdateReminderSnoozed(AppUpdateInfo updateInfo) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String version = prefs.getString(PREF_APP_UPDATE_REMIND_VERSION, "");
        long remindAfter = prefs.getLong(PREF_APP_UPDATE_REMIND_AFTER, 0L);
        return updateInfo.latestVersion.equals(version) && remindAfter > System.currentTimeMillis();
    }

    private void showMandatoryUpdateDialog(AppUpdateInfo updateInfo) {
        AppUpdateInfo info = updateInfo == null ? storedMandatoryUpdateInfo() : updateInfo;
        if (info == null) {
            return;
        }
        if (mandatoryUpdateDialog != null && mandatoryUpdateDialog.isShowing()) {
            return;
        }
        mandatoryUpdateRequired = true;
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(ui("Zorunlu güncelleme"))
                .setMessage(ui("Bu sürüm artık kullanılamaz.") + "\n\n" +
                        ui("Kurulu sürüm") + ": " + info.currentVersion + "\n" +
                        ui("Yeni sürüm") + ": " + info.latestLabel() + "\n\n" +
                        ui("Devam etmek için uygulamayı güncelleyin."))
                .setPositiveButton(ui(TextUtils.isEmpty(info.apkUrl) ? "Release sayfasını aç" : "İndir ve kur"), null)
                .setNeutralButton(ui("Release sayfasını aç"), null)
                .setNegativeButton(ui("Uygulamayı kapat"), (dialogInterface, which) -> finish())
                .create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(dismissed -> {
            if (mandatoryUpdateDialog == dialog) {
                mandatoryUpdateDialog = null;
            }
        });
        dialog.setOnShowListener(shown -> {
            Button installButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            if (installButton != null) {
                installButton.setOnClickListener(v -> {
                    if (TextUtils.isEmpty(info.apkUrl)) {
                        openWebsite(info.htmlUrl);
                    } else {
                        dialog.dismiss();
                        openUpdateTarget(info);
                    }
                });
            }
            Button releaseButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            if (releaseButton != null) {
                releaseButton.setVisibility(TextUtils.isEmpty(info.apkUrl) ? View.GONE : View.VISIBLE);
                releaseButton.setOnClickListener(v -> openWebsite(info.htmlUrl));
            }
        });
        mandatoryUpdateDialog = dialog;
        dialog.show();
    }

    private String currentAppVersion() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception ignored) {
            return "3.15";
        }
    }

    private static String normalizeVersionTag(String value) {
        if (value == null) {
            return "";
        }
        String version = value.trim();
        if (version.startsWith("v") || version.startsWith("V")) {
            version = version.substring(1);
        }
        int dash = version.indexOf('-');
        if (dash > 0) {
            version = version.substring(0, dash);
        }
        return version;
    }

    private static int compareVersions(String left, String right) {
        String[] leftParts = numericVersionParts(left);
        String[] rightParts = numericVersionParts(right);
        int count = Math.max(leftParts.length, rightParts.length);
        for (int i = 0; i < count; i++) {
            int leftValue = i < leftParts.length ? parsePositiveInt(leftParts[i], 0) : 0;
            int rightValue = i < rightParts.length ? parsePositiveInt(rightParts[i], 0) : 0;
            if (leftValue != rightValue) {
                return leftValue - rightValue;
            }
        }
        return 0;
    }

    private static String[] numericVersionParts(String version) {
        if (TextUtils.isEmpty(version)) {
            return new String[]{"0"};
        }
        String cleaned = version.replaceFirst("^[^0-9]+", "");
        if (TextUtils.isEmpty(cleaned)) {
            return new String[]{"0"};
        }
        return cleaned.split("[^0-9]+");
    }

    private void authenticateWithFirebase(boolean register, String rawEmail, String password, String repeatPassword) {
        String email = normalizeEmail(rawEmail);
        if (!isValidEmail(email)) {
            toast("Ge\u00e7erli bir e-posta girin");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            toast("\u015eifre en az 6 karakter olmal\u0131");
            return;
        }
        if (register && !password.equals(repeatPassword)) {
            toast("\u015eifreler e\u015fle\u015fmiyor");
            return;
        }
        if (!isFirestoreLicenseConfigured()) {
            showFirestoreSetupDialog();
            return;
        }

        setBusy(true, register ? "Kay\u0131t olu\u015fturuluyor..." : "Giri\u015f yap\u0131l\u0131yor...");
        licenseExecutor.execute(() -> {
            try {
                AuthSession session = requestFirebaseAuth(register, email, password);
                saveAuthSession(session);
                LicenseResult result = requestLicenseRegistration(session.email);
                mainHandler.post(() -> {
                    setBusy(false, null);
                    applyLicenseResult(result);
                    if (settingsPanel != null && settingsPanel.getVisibility() == View.VISIBLE) {
                        renderLicenseSettings(settingsPanel);
                    }
                    showHome();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setTitle(ui(result.active ? "Lisans etkin" : "Onay bekleniyor"))
                            .setMessage(ui(result.active
                                    ? "Hesab\u0131n\u0131z onayl\u0131. Uygulamay\u0131 kullanabilirsiniz."
                                    : "Kay\u0131t iste\u011finiz al\u0131nd\u0131. Y\u00f6netici onay\u0131ndan sonra indirme \u00f6zellikleri a\u00e7\u0131l\u0131r."));
                    if (!result.active) {
                        builder.setNeutralButton(ui("Lisans satın al"), (dialog, which) -> openWhatsAppLicensePurchase(session.email));
                    }
                    builder.setPositiveButton(ui("Tamam"), null).show();
                });
            } catch (Exception error) {
                mainHandler.post(() -> {
                    setBusy(false, null);
                    outputView.setText(cleanError(error));
                    new AlertDialog.Builder(this)
                            .setTitle(ui(register ? "Kay\u0131t ba\u015far\u0131s\u0131z" : "Giri\u015f ba\u015far\u0131s\u0131z"))
                            .setMessage(ui(cleanError(error)))
                            .setPositiveButton(ui("Tamam"), null)
                            .show();
                });
            }
        });
    }

    private void sendPasswordReset(String rawEmail) {
        String email = normalizeEmail(rawEmail);
        if (!isValidEmail(email)) {
            toast("Ge\u00e7erli bir e-posta girin");
            return;
        }
        if (!isFirestoreLicenseConfigured()) {
            showFirestoreSetupDialog();
            return;
        }
        setBusy(true, "\u015eifre s\u0131f\u0131rlama e-postas\u0131 g\u00f6nderiliyor...");
        licenseExecutor.execute(() -> {
            try {
                requestFirebasePasswordReset(email);
                mainHandler.post(() -> {
                    setBusy(false, null);
                    new AlertDialog.Builder(this)
                            .setTitle(ui("\u015eifre s\u0131f\u0131rlama"))
                            .setMessage(ui("\u015eifre s\u0131f\u0131rlama ba\u011flant\u0131s\u0131 e-posta adresinize g\u00f6nderildi."))
                            .setPositiveButton(ui("Tamam"), null)
                            .show();
                });
            } catch (Exception error) {
                mainHandler.post(() -> {
                    setBusy(false, null);
                    new AlertDialog.Builder(this)
                            .setTitle(ui("\u015eifre s\u0131f\u0131rlama ba\u015far\u0131s\u0131z"))
                            .setMessage(ui(cleanError(error)))
                            .setPositiveButton(ui("Tamam"), null)
                            .show();
                });
            }
        });
    }

    private AuthSession requestFirebaseAuth(boolean register, String email, String password) throws Exception {
        JSONObject body = new JSONObject();
        body.put("email", email);
        body.put("password", password);
        body.put("returnSecureToken", true);
        JSONObject response = firebaseAuthPost(register ? "accounts:signUp" : "accounts:signInWithPassword", body);
        return new AuthSession(
                normalizeEmail(response.optString("email", email)),
                response.optString("idToken", ""),
                response.optString("refreshToken", ""),
                response.optString("localId", ""),
                firebaseTokenExpiresAt(response, "expiresIn")
        );
    }

    private AuthSession requestFirebaseTokenRefresh(String refreshToken, String fallbackEmail) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(FIREBASE_TOKEN_REFRESH_URL + "?key=" + FIREBASE_WEB_API_KEY).openConnection();
        connection.setConnectTimeout(12000);
        connection.setReadTimeout(12000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("User-Agent", "MetaFoldDownloader/" + currentAppVersion());

        String form = "grant_type=refresh_token&refresh_token="
                + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8.name());
        try (OutputStream output = connection.getOutputStream()) {
            output.write(form.getBytes(StandardCharsets.UTF_8));
        }

        int code = connection.getResponseCode();
        String response = readHttpBody(code >= 200 && code < 300 ? connection.getInputStream() : connection.getErrorStream());
        if (code < 200 || code >= 300) {
            throw new IOException(firebaseAuthErrorMessage(response));
        }

        JSONObject json = TextUtils.isEmpty(response) ? new JSONObject() : new JSONObject(response);
        return new AuthSession(
                normalizeEmail(fallbackEmail),
                json.optString("id_token", ""),
                json.optString("refresh_token", refreshToken),
                json.optString("user_id", getString(PREF_AUTH_LOCAL_ID, "")),
                firebaseTokenExpiresAt(json, "expires_in")
        );
    }

    private static long firebaseTokenExpiresAt(JSONObject response, String fieldName) {
        long seconds = 3600L;
        try {
            String raw = response.optString(fieldName, "3600");
            if (!TextUtils.isEmpty(raw)) {
                seconds = Long.parseLong(raw);
            }
        } catch (Exception ignored) {
            seconds = 3600L;
        }
        seconds = Math.max(60L, seconds);
        return System.currentTimeMillis() + (seconds * 1000L);
    }

    private void requestFirebasePasswordReset(String email) throws Exception {
        JSONObject body = new JSONObject();
        body.put("requestType", "PASSWORD_RESET");
        body.put("email", email);
        firebaseAuthPost("accounts:sendOobCode", body);
    }

    private JSONObject firebaseAuthPost(String method, JSONObject body) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(firebaseAuthUrl(method)).openConnection();
        connection.setConnectTimeout(12000);
        connection.setReadTimeout(12000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("User-Agent", "MetaFoldDownloader/" + currentAppVersion());
        try (OutputStream output = connection.getOutputStream()) {
            output.write(body.toString().getBytes(StandardCharsets.UTF_8));
        }
        int code = connection.getResponseCode();
        String response = readHttpBody(code >= 200 && code < 300 ? connection.getInputStream() : connection.getErrorStream());
        if (code < 200 || code >= 300) {
            throw new IOException(firebaseAuthErrorMessage(response));
        }
        return TextUtils.isEmpty(response) ? new JSONObject() : new JSONObject(response);
    }

    private String firebaseAuthUrl(String method) {
        return FIREBASE_AUTH_BASE_URL + method + "?key=" + FIREBASE_WEB_API_KEY;
    }

    private String firebaseAuthErrorMessage(String response) {
        String message = "";
        try {
            JSONObject root = new JSONObject(response);
            JSONObject error = root.optJSONObject("error");
            if (error != null) {
                message = error.optString("message", "");
            }
        } catch (Exception ignored) {
            message = response;
        }
        if (message.contains("EMAIL_EXISTS")) {
            return "Bu e-posta zaten kay\u0131tl\u0131. Giri\u015f yap\u0131n.";
        }
        if (message.contains("EMAIL_NOT_FOUND")) {
            return "Bu e-posta ile hesap bulunamad\u0131.";
        }
        if (message.contains("INVALID_PASSWORD") || message.contains("INVALID_LOGIN_CREDENTIALS")) {
            return "E-posta veya \u015fifre hatal\u0131.";
        }
        if (message.contains("WEAK_PASSWORD")) {
            return "\u015eifre en az 6 karakter olmal\u0131.";
        }
        if (message.contains("USER_DISABLED")) {
            return "Bu kullan\u0131c\u0131 hesab\u0131 devre d\u0131\u015f\u0131.";
        }
        if (message.contains("TOKEN_EXPIRED")
                || message.contains("INVALID_REFRESH_TOKEN")
                || message.contains("USER_NOT_FOUND")
                || message.contains("INVALID_ID_TOKEN")) {
            return firebaseSessionExpiredMessage();
        }
        if (message.contains("TOO_MANY_ATTEMPTS_TRY_LATER")) {
            return "\u00c7ok fazla deneme yap\u0131ld\u0131. Biraz sonra tekrar deneyin.";
        }
        if (message.contains("CONFIGURATION_NOT_FOUND") || message.contains("OPERATION_NOT_ALLOWED")) {
            return "Firebase Authentication kurulumu bulunamad\u0131. Firebase Console'da Authentication > Sign-in method b\u00f6l\u00fcm\u00fcnden Email/Password giri\u015fini etkinle\u015ftirin.";
        }
        if (message.contains("API key not valid")) {
            return "Firebase Web API Key ge\u00e7ersiz veya bu proje i\u00e7in de\u011fil.";
        }
        return TextUtils.isEmpty(message) ? "Firebase oturum hatas\u0131." : message;
    }

    private void saveAuthSession(AuthSession session) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(PREF_AUTH_EMAIL, session.email)
                .putString(PREF_AUTH_ID_TOKEN, session.idToken)
                .putString(PREF_AUTH_REFRESH_TOKEN, session.refreshToken)
                .putString(PREF_AUTH_LOCAL_ID, session.localId)
                .putLong(PREF_AUTH_TOKEN_EXPIRES_AT, session.expiresAt)
                .putString(PREF_LICENSE_EMAIL, session.email)
                .commit();
    }

    private void clearAuthSession() {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .remove(PREF_AUTH_EMAIL)
                .remove(PREF_AUTH_ID_TOKEN)
                .remove(PREF_AUTH_REFRESH_TOKEN)
                .remove(PREF_AUTH_LOCAL_ID)
                .remove(PREF_AUTH_TOKEN_EXPIRES_AT)
                .remove(PREF_LICENSE_KEY)
                .remove(PREF_LICENSE_EMAIL)
                .remove(PREF_LICENSE_REQUEST_ID)
                .remove(PREF_LICENSE_STATUS)
                .remove(PREF_LICENSE_OWNER)
                .remove(PREF_LICENSE_EXPIRES_AT)
                .remove(PREF_LICENSE_BOUND_DEVICE_ID)
                .remove(PREF_LICENSE_BOUND_DEVICE_LABEL)
                .remove(PREF_LICENSE_NEXT_DEVICE_CHANGE)
                .remove(PREF_LICENSE_LAST_CHECK)
                .apply();
    }

    private boolean isSignedIn() {
        return !TextUtils.isEmpty(getString(PREF_AUTH_ID_TOKEN, ""))
                && !TextUtils.isEmpty(getString(PREF_AUTH_EMAIL, ""));
    }

    private String currentAuthEmail() {
        return getString(PREF_AUTH_EMAIL, "");
    }

    private void signOut() {
        clearAuthSession();
        toast("\u00c7\u0131k\u0131\u015f yap\u0131ld\u0131");
        showAuthPanel(false);
    }

    private void showAuthRequiredDialog() {
        new AlertDialog.Builder(this)
                .setTitle(ui("Giri\u015f gerekli"))
                .setMessage(ui("Devam etmek i\u00e7in kay\u0131t olun veya giri\u015f yap\u0131n."))
                .setPositiveButton(ui("Giri\u015f yap"), (dialog, which) -> showAuthPanel(true))
                .setNeutralButton(ui("Kay\u0131t ol"), (dialog, which) -> showAuthPanel(false))
                .setNegativeButton(ui("Vazge\u00e7"), null)
                .show();
    }

    private void showLicenseDialog(LinearLayout panel) {
        EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        input.setText(getString(PREF_LICENSE_KEY, ""));
        input.setSelectAllOnFocus(true);

        new AlertDialog.Builder(this)
                .setTitle(ui("Lisans anahtarı"))
                .setMessage(ui("Satın alınan lisans anahtarını girin. Anahtar bu cihaza bağlanarak doğrulanır."))
                .setView(input)
                .setPositiveButton(ui("Kaydet ve doğrula"), (dialog, which) -> {
                    String key = normalizeLicenseKey(input.getText().toString());
                    if (TextUtils.isEmpty(key)) {
                        toast("Lisans anahtarı boş olamaz");
                        return;
                    }
                    putString(PREF_LICENSE_KEY, key);
                    putString(PREF_LICENSE_STATUS, LICENSE_STATUS_PENDING);
                    renderLicenseSettings(panel);
                    validateLicense(false, panel);
                })
                .setNeutralButton(ui("Temizle"), (dialog, which) -> {
                    clearLicense();
                    renderLicenseSettings(panel);
                })
                .setNegativeButton(ui("Vazgeç"), null)
                .show();
    }

    private void showLicenseRegistrationDialog(LinearLayout panel) {
        EditText emailInput = new EditText(this);
        emailInput.setSingleLine(true);
        emailInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailInput.setText(getString(PREF_LICENSE_EMAIL, ""));
        emailInput.setHint(ui("E-posta adresi"));
        emailInput.setSelectAllOnFocus(true);

        new AlertDialog.Builder(this)
                .setTitle(ui("E-posta ile kayıt"))
                .setMessage(ui("E-posta adresiniz ve cihaz kimliğiniz yönetici onayına gönderilecek. Onay verilince uygulama kullanılabilir."))
                .setView(emailInput)
                .setPositiveButton(ui("Kayıt isteği gönder"), (dialog, which) -> {
                    String email = normalizeEmail(emailInput.getText().toString());
                    if (!isValidEmail(email)) {
                        toast("Geçerli bir e-posta girin");
                        return;
                    }
                    registerLicenseEmail(email, panel);
                })
                .setNegativeButton(ui("Vazgeç"), null)
                .show();
    }

    private void registerLicenseEmail(String email, LinearLayout panel) {
        if (!isSignedIn()) {
            showAuthPanel(false);
            return;
        }
        putString(PREF_LICENSE_EMAIL, email);
        putString(PREF_LICENSE_STATUS, LICENSE_STATUS_PENDING);
        if (!isFirestoreLicenseConfigured()) {
            showFirestoreSetupDialog();
            renderLicenseSettings(panel);
            return;
        }

        setBusy(true, "Kayıt isteği gönderiliyor...");
        licenseExecutor.execute(() -> {
            try {
                LicenseResult result = requestLicenseRegistration(email);
                mainHandler.post(() -> {
                    setBusy(false, null);
                    applyLicenseResult(result);
                    renderLicenseSettings(panel);
                    new AlertDialog.Builder(this)
                            .setTitle(ui(result.active ? "Lisans etkin" : "Onay bekleniyor"))
                            .setMessage(ui(result.message))
                            .setPositiveButton(ui("Tamam"), null)
                            .show();
                });
            } catch (Exception error) {
                mainHandler.post(() -> {
                    setBusy(false, null);
                    outputView.setText(cleanError(error));
                    toast("Kayıt isteği gönderilemedi");
                });
            }
        });
    }

    private void validateLicense(boolean silent, LinearLayout panel) {
        if (!isSignedIn()) {
            if (!silent) {
                showAuthPanel(true);
            }
            return;
        }
        String key = getString(PREF_LICENSE_KEY, "").trim();
        String email = getString(PREF_LICENSE_EMAIL, "").trim();
        if (TextUtils.isEmpty(key) && TextUtils.isEmpty(email)) {
            if (!silent) {
                toast("Önce e-posta ile kayıt olun");
                showLicenseRegistrationDialog(panel);
            }
            return;
        }
        if (!isFirestoreLicenseConfigured()) {
            if (!silent) {
                showFirestoreSetupDialog();
                renderLicenseSettings(panel);
            }
            return;
        }

        if (!silent) {
            setBusy(true, "Lisans doğrulanıyor...");
        }
        licenseExecutor.execute(() -> {
            try {
                LicenseResult result = requestLicenseValidation(key, email);
                mainHandler.post(() -> {
                    if (!silent) {
                        setBusy(false, null);
                    }
                    applyLicenseResult(result);
                    if (!silent) {
                        renderLicenseSettings(panel);
                        new AlertDialog.Builder(this)
                                .setTitle(ui(result.active ? "Lisans etkin" : "Lisans doğrulanamadı"))
                                .setMessage(ui(result.message))
                                .setPositiveButton(ui("Tamam"), null)
                                .show();
                    }
                });
            } catch (Exception error) {
                mainHandler.post(() -> {
                    if (!silent) {
                        setBusy(false, null);
                        outputView.setText(cleanError(error));
                        toast("Lisans doğrulanamadı");
                    }
                });
            }
        });
    }

    private void refreshLicenseSilently(boolean blockMessage) {
        if (licenseRefreshInProgress || !isSignedIn() || !isFirestoreLicenseConfigured()) {
            return;
        }
        String email = getString(PREF_LICENSE_EMAIL, "").trim();
        if (TextUtils.isEmpty(email)) {
            email = currentAuthEmail();
        }
        if (TextUtils.isEmpty(email)) {
            return;
        }
        licenseRefreshInProgress = true;
        final String checkedEmail = email;
        licenseExecutor.execute(() -> {
            try {
                LicenseResult result = requestLicenseValidation("", checkedEmail);
                mainHandler.post(() -> {
                    licenseRefreshInProgress = false;
                    applyLicenseResult(result);
                    if (settingsPanel != null && settingsPanel.getVisibility() == View.VISIBLE) {
                        renderLicenseSettings(settingsPanel);
                    }
                    if (blockMessage && !result.active) {
                        toast(result.message);
                    }
                });
            } catch (Exception ignored) {
                mainHandler.post(() -> {
                    licenseRefreshInProgress = false;
                    if (blockMessage) {
                        toast("Lisans doğrulanamadı");
                    }
                });
            }
        });
    }

    private boolean needsLicenseRefresh() {
        long lastCheck = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getLong(PREF_LICENSE_LAST_CHECK, 0L);
        return lastCheck <= 0L || System.currentTimeMillis() - lastCheck > LICENSE_REFRESH_INTERVAL_MS;
    }

    private LicenseResult requestLicenseRegistration(String email) throws Exception {
        String docId = licenseDocumentId(email);
        long now = System.currentTimeMillis();
        JSONObject body = new JSONObject();
        JSONObject fields = new JSONObject();
        putFirestoreString(fields, "email", email);
        putFirestoreString(fields, "deviceId", deviceId());
        putFirestoreString(fields, "deviceLabel", Build.MANUFACTURER + " " + Build.MODEL);
        putFirestoreString(fields, "packageName", getPackageName());
        putFirestoreString(fields, "appVersion", currentAppVersion());
        putFirestoreString(fields, "status", LICENSE_STATUS_PENDING);
        putFirestoreString(fields, "requestId", requestIdForDocument(docId));
        putFirestoreString(fields, "owner", "");
        putFirestoreString(fields, "expiresAt", "");
        putFirestoreString(fields, "licenseKey", "");
        putFirestoreString(fields, "createdAt", String.valueOf(System.currentTimeMillis()));
        putFirestoreTimestamp(fields, "lastDeviceChangeAt", now);
        putFirestoreTimestamp(fields, "nextDeviceChangeAt", now + DEVICE_CHANGE_COOLDOWN_MS);
        body.put("fields", fields);

        FirestoreResponse response = firestorePost(firestoreCollectionUrl(docId), body);
        if (response.code == HttpURLConnection.HTTP_CONFLICT || response.code == 409) {
            return requestLicenseValidation("", email);
        }
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED || response.code == 401) {
            throw new IOException(firebaseSessionExpiredMessage());
        }
        if (response.code == HttpURLConnection.HTTP_FORBIDDEN || response.code == 403) {
            throw new IOException(firestorePermissionMessage());
        }
        if (response.code < 200 || response.code >= 300) {
            throw new IOException("Firestore kayıt hatası HTTP " + response.code + ": " + response.body);
        }
        return enforceDeviceBinding(licenseResultFromFirestore(new JSONObject(response.body), email, docId), docId);
    }

    private LicenseResult requestLicenseValidation(String key, String email) throws Exception {
        String docId = licenseDocumentId(email);
        FirestoreResponse response = firestoreGet(firestoreDocumentUrl(docId));
        if (response.code == HttpURLConnection.HTTP_NOT_FOUND || response.code == 404) {
            return new LicenseResult(false, LICENSE_STATUS_PENDING, "Onay bekleniyor", "", "", "", email, requestIdForDocument(docId), "", "", 0L);
        }
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED || response.code == 401) {
            throw new IOException(firebaseSessionExpiredMessage());
        }
        if (response.code == HttpURLConnection.HTTP_FORBIDDEN || response.code == 403) {
            throw new IOException(firestorePermissionMessage());
        }
        if (response.code < 200 || response.code >= 300) {
            throw new IOException("Firestore lisans kontrol hatası HTTP " + response.code + ": " + response.body);
        }
        return enforceDeviceBinding(licenseResultFromFirestore(new JSONObject(response.body), email, docId), docId);
    }

    private LicenseResult licenseResultFromFirestore(JSONObject document, String fallbackEmail, String docId) {
        JSONObject fields = document.optJSONObject("fields");
        String responseStatus = firestoreString(fields, "status");
        boolean active = LICENSE_STATUS_ACTIVE.equalsIgnoreCase(responseStatus);
        String status = active ? LICENSE_STATUS_ACTIVE : responseStatus;
        if (!LICENSE_STATUS_INACTIVE.equals(status) && !LICENSE_STATUS_ACTIVE.equals(status) && !LICENSE_STATUS_DEVICE_LOCKED.equals(status)) {
            status = LICENSE_STATUS_PENDING;
        }
        String owner = firestoreString(fields, "owner");
        String expiresAt = firestoreStringOrTimestamp(fields, "expiresAt");
        if (active && isLicenseExpired(expiresAt)) {
            active = false;
            status = LICENSE_STATUS_INACTIVE;
        }
        String message = active ? "Lisans etkin" : (isLicenseExpired(expiresAt) ? "Lisans süresi doldu" : ("inactive".equals(status) ? "Lisans pasif" : "Onay bekleniyor"));
        String licenseKey = normalizeLicenseKey(firestoreString(fields, "licenseKey"));
        String email = normalizeEmail(firstNonEmpty(firestoreString(fields, "email"), fallbackEmail));
        String requestId = firstNonEmpty(firestoreString(fields, "requestId"), requestIdForDocument(docId));
        String boundDeviceId = firestoreString(fields, "deviceId");
        String boundDeviceLabel = firestoreString(fields, "deviceLabel");
        long nextDeviceChangeAt = firestoreTimestampMillis(fields, "nextDeviceChangeAt");
        return new LicenseResult(active, status, message, owner, expiresAt, licenseKey, email, requestId, boundDeviceId, boundDeviceLabel, nextDeviceChangeAt);
    }

    private LicenseResult enforceDeviceBinding(LicenseResult result, String docId) throws Exception {
        if (!result.active || TextUtils.isEmpty(result.boundDeviceId) || deviceId().equals(result.boundDeviceId)) {
            return result;
        }
        long now = System.currentTimeMillis();
        if (result.nextDeviceChangeAt > now) {
            return new LicenseResult(
                    false,
                    LICENSE_STATUS_DEVICE_LOCKED,
                    "Bu lisans başka bir cihaza bağlı. Cihaz değişimi " + formatDateTime(result.nextDeviceChangeAt) + " sonrasında yapılabilir.",
                    result.owner,
                    result.expiresAt,
                    result.licenseKey,
                    result.email,
                    result.requestId,
                    result.boundDeviceId,
                    result.boundDeviceLabel,
                    result.nextDeviceChangeAt
            );
        }
        return requestDeviceTransfer(docId, result);
    }

    private LicenseResult requestDeviceTransfer(String docId, LicenseResult previous) throws Exception {
        long now = System.currentTimeMillis();
        JSONObject body = new JSONObject();
        JSONObject fields = new JSONObject();
        putFirestoreString(fields, "deviceId", deviceId());
        putFirestoreString(fields, "deviceLabel", Build.MANUFACTURER + " " + Build.MODEL);
        putFirestoreString(fields, "appVersion", currentAppVersion());
        putFirestoreTimestamp(fields, "lastDeviceChangeAt", now);
        putFirestoreTimestamp(fields, "nextDeviceChangeAt", now + DEVICE_CHANGE_COOLDOWN_MS);
        body.put("fields", fields);

        FirestoreResponse response = firestorePatch(
                firestoreDocumentUrl(docId),
                body,
                "deviceId",
                "deviceLabel",
                "appVersion",
                "lastDeviceChangeAt",
                "nextDeviceChangeAt"
        );
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED || response.code == 401) {
            throw new IOException(firebaseSessionExpiredMessage());
        }
        if (response.code == HttpURLConnection.HTTP_FORBIDDEN || response.code == 403) {
            throw new IOException(firestorePermissionMessage());
        }
        if (response.code < 200 || response.code >= 300) {
            return new LicenseResult(
                    false,
                    LICENSE_STATUS_DEVICE_LOCKED,
                    "Cihaz değişimi şu anda yapılamıyor. Yeniden denemeden önce lisans ekranından onay durumunu kontrol edin.",
                    previous.owner,
                    previous.expiresAt,
                    previous.licenseKey,
                    previous.email,
                    previous.requestId,
                    previous.boundDeviceId,
                    previous.boundDeviceLabel,
                    previous.nextDeviceChangeAt
            );
        }
        LicenseResult transferred = licenseResultFromFirestore(new JSONObject(response.body), previous.email, docId);
        return new LicenseResult(
                transferred.active,
                transferred.status,
                "Cihaz değişimi yapıldı. Bu hesap 7 gün boyunca bu cihaza bağlı kalacak.",
                transferred.owner,
                transferred.expiresAt,
                transferred.licenseKey,
                transferred.email,
                transferred.requestId,
                transferred.boundDeviceId,
                transferred.boundDeviceLabel,
                transferred.nextDeviceChangeAt
        );
    }

    private void applyLicenseResult(LicenseResult result) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                .putString(PREF_LICENSE_STATUS, result.status)
                .putString(PREF_LICENSE_OWNER, result.owner)
                .putString(PREF_LICENSE_EXPIRES_AT, result.expiresAt)
                .putString(PREF_LICENSE_BOUND_DEVICE_ID, result.boundDeviceId)
                .putString(PREF_LICENSE_BOUND_DEVICE_LABEL, result.boundDeviceLabel)
                .putLong(PREF_LICENSE_NEXT_DEVICE_CHANGE, result.nextDeviceChangeAt)
                .putLong(PREF_LICENSE_LAST_CHECK, System.currentTimeMillis());
        if (!TextUtils.isEmpty(result.licenseKey)) {
            editor.putString(PREF_LICENSE_KEY, result.licenseKey);
        }
        if (!TextUtils.isEmpty(result.email)) {
            editor.putString(PREF_LICENSE_EMAIL, result.email);
        }
        if (!TextUtils.isEmpty(result.requestId)) {
            editor.putString(PREF_LICENSE_REQUEST_ID, result.requestId);
        }
        editor.apply();
    }

    private void clearLicense() {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .remove(PREF_LICENSE_KEY)
                .remove(PREF_LICENSE_EMAIL)
                .remove(PREF_LICENSE_REQUEST_ID)
                .remove(PREF_LICENSE_STATUS)
                .remove(PREF_LICENSE_OWNER)
                .remove(PREF_LICENSE_EXPIRES_AT)
                .remove(PREF_LICENSE_BOUND_DEVICE_ID)
                .remove(PREF_LICENSE_BOUND_DEVICE_LABEL)
                .remove(PREF_LICENSE_NEXT_DEVICE_CHANGE)
                .remove(PREF_LICENSE_LAST_CHECK)
                .apply();
    }

    private String licenseStatusText() {
        String status = getString(PREF_LICENSE_STATUS, "");
        String expiresAt = getString(PREF_LICENSE_EXPIRES_AT, "");
        if (LICENSE_STATUS_ACTIVE.equals(status) && isLicenseExpired(expiresAt)) {
            return TextUtils.isEmpty(expiresAt) ? "Lisans süresi doldu" : "Lisans süresi doldu - " + expiresAt;
        }
        if (LICENSE_STATUS_ACTIVE.equals(status)) {
            String owner = getString(PREF_LICENSE_OWNER, "");
            String text = "Etkin";
            if (!TextUtils.isEmpty(owner)) {
                text += " - " + owner;
            }
            if (!TextUtils.isEmpty(expiresAt)) {
                text += " - " + expiresAt;
            }
            return text;
        }
        if (LICENSE_STATUS_PENDING.equals(status)) {
            return "Anahtar kaydedildi, sunucu doğrulaması bekleniyor";
        }
        if (LICENSE_STATUS_DEVICE_LOCKED.equals(status)) {
            long nextChange = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getLong(PREF_LICENSE_NEXT_DEVICE_CHANGE, 0L);
            return nextChange > 0
                    ? "Bu lisans başka cihaza bağlı. Değişim zamanı: " + formatDateTime(nextChange)
                    : "Bu lisans başka cihaza bağlı";
        }
        if (LICENSE_STATUS_INACTIVE.equals(status)) {
            return "Pasif veya doğrulanamadı";
        }
        return "Lisans girilmedi";
    }

    private String licenseExpiryLabel() {
        String expiresAt = getString(PREF_LICENSE_EXPIRES_AT, "");
        if (TextUtils.isEmpty(expiresAt)) {
            return "Süresiz";
        }
        return isLicenseExpired(expiresAt) ? "Süresi doldu - " + expiresAt : "Geçerli - " + expiresAt;
    }

    private String licenseEmailLabel() {
        String email = getString(PREF_LICENSE_EMAIL, "");
        if (TextUtils.isEmpty(email)) {
            return "Henüz e-posta ile kayıt olunmadı";
        }
        String requestId = getString(PREF_LICENSE_REQUEST_ID, "");
        return TextUtils.isEmpty(requestId) ? email : email + " - " + requestId;
    }

    private String licenseDeviceLabel() {
        String boundDeviceId = getString(PREF_LICENSE_BOUND_DEVICE_ID, "");
        if (TextUtils.isEmpty(boundDeviceId)) {
            return "Henüz cihaz bağlanmadı";
        }
        String label = getString(PREF_LICENSE_BOUND_DEVICE_LABEL, "");
        String current = deviceId().equals(boundDeviceId) ? "Bu cihaz" : "Başka cihaz";
        return TextUtils.isEmpty(label) ? current : current + " - " + label;
    }

    private String licenseDeviceChangeLabel() {
        long nextChange = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getLong(PREF_LICENSE_NEXT_DEVICE_CHANGE, 0L);
        if (nextChange <= 0L) {
            return "Kayıt sonrası 7 gün kilitlenir";
        }
        long now = System.currentTimeMillis();
        if (nextChange <= now) {
            return "Cihaz değişimi yapılabilir";
        }
        return "Yeni cihaz için " + formatDateTime(nextChange) + " sonrası";
    }

    private String licenseKeyLabel() {
        String key = getString(PREF_LICENSE_KEY, "");
        if (TextUtils.isEmpty(key)) {
            return "Henüz lisans anahtarı girilmedi";
        }
        String suffix = key.length() <= 6 ? key : key.substring(key.length() - 6);
        return "Anahtar: ******" + suffix;
    }

    private boolean isFirestoreLicenseConfigured() {
        return !TextUtils.isEmpty(FIREBASE_PROJECT_ID) && !TextUtils.isEmpty(FIREBASE_WEB_API_KEY);
    }

    private void showFirestoreSetupDialog() {
        new AlertDialog.Builder(this)
                .setTitle(ui("Firebase ayarı eksik"))
                .setMessage(ui("Spark plan lisans sistemi için Firebase Web API Key uygulamaya eklenmeli."))
                .setPositiveButton(ui("Tamam"), null)
                .show();
    }

    private String licenseDocumentId(String email) {
        return sha256Hex(normalizeEmail(email));
    }

    private static String requestIdForDocument(String docId) {
        if (TextUtils.isEmpty(docId)) {
            return "REQ-UNKNOWN";
        }
        return "REQ-" + docId.substring(0, Math.min(12, docId.length())).toUpperCase(Locale.US);
    }

    private String firestoreCollectionUrl(String docId) {
        return firestoreBaseUrl() + "?documentId=" + docId + "&key=" + FIREBASE_WEB_API_KEY;
    }

    private String firestoreDocumentUrl(String docId) {
        return firestoreBaseUrl() + "/" + docId + "?key=" + FIREBASE_WEB_API_KEY;
    }

    private String firestoreBaseUrl() {
        return "https://firestore.googleapis.com/v1/projects/"
                + FIREBASE_PROJECT_ID
                + "/databases/(default)/documents/"
                + FIRESTORE_LICENSE_COLLECTION;
    }

    private String firestorePermissionMessage() {
        return "Firestore izinleri kayıt isteğini reddetti. Firebase Console > Firestore > Rules ekranında uygulamanın güncel firestore.rules içeriğini Publish edin.";
    }

    private static String firebaseSessionExpiredMessage() {
        return "Oturum süresi doldu. Lütfen çıkış yapıp tekrar giriş yapın.";
    }

    private synchronized void ensureFreshFirebaseAuthToken(boolean forceRefresh) throws IOException {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String token = prefs.getString(PREF_AUTH_ID_TOKEN, "");
        long expiresAt = prefs.getLong(PREF_AUTH_TOKEN_EXPIRES_AT, 0L);
        long refreshBefore = System.currentTimeMillis() + (2L * 60L * 1000L);
        if (!forceRefresh && !TextUtils.isEmpty(token) && expiresAt > refreshBefore) {
            return;
        }

        String refreshToken = prefs.getString(PREF_AUTH_REFRESH_TOKEN, "");
        if (TextUtils.isEmpty(refreshToken)) {
            throw new IOException(firebaseSessionExpiredMessage());
        }

        try {
            AuthSession session = requestFirebaseTokenRefresh(refreshToken, prefs.getString(PREF_AUTH_EMAIL, ""));
            saveAuthSession(session);
        } catch (Exception error) {
            throw new IOException(firebaseSessionExpiredMessage(), error);
        }
    }

    private void setFirebaseAuthHeader(HttpURLConnection connection) {
        String token = getString(PREF_AUTH_ID_TOKEN, "");
        if (!TextUtils.isEmpty(token)) {
            connection.setRequestProperty("Authorization", "Bearer " + token);
        }
    }

    private FirestoreResponse firestoreGet(String url) throws IOException {
        ensureFreshFirebaseAuthToken(false);
        FirestoreResponse response = firestoreGetOnce(url);
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED || response.code == 401) {
            ensureFreshFirebaseAuthToken(true);
            response = firestoreGetOnce(url);
        }
        return response;
    }

    private FirestoreResponse firestoreGetOnce(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setConnectTimeout(12000);
        connection.setReadTimeout(12000);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("User-Agent", "MetaFoldDownloader/" + currentAppVersion());
        setFirebaseAuthHeader(connection);
        int code = connection.getResponseCode();
        String body = readHttpBody(code >= 200 && code < 300 ? connection.getInputStream() : connection.getErrorStream());
        return new FirestoreResponse(code, body);
    }

    private FirestoreResponse firestorePost(String url, JSONObject body) throws IOException {
        ensureFreshFirebaseAuthToken(false);
        FirestoreResponse response = firestorePostOnce(url, body);
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED || response.code == 401) {
            ensureFreshFirebaseAuthToken(true);
            response = firestorePostOnce(url, body);
        }
        return response;
    }

    private FirestoreResponse firestorePostOnce(String url, JSONObject body) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setConnectTimeout(12000);
        connection.setReadTimeout(12000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("User-Agent", "MetaFoldDownloader/" + currentAppVersion());
        setFirebaseAuthHeader(connection);
        try (OutputStream output = connection.getOutputStream()) {
            output.write(body.toString().getBytes(StandardCharsets.UTF_8));
        }
        int code = connection.getResponseCode();
        String response = readHttpBody(code >= 200 && code < 300 ? connection.getInputStream() : connection.getErrorStream());
        return new FirestoreResponse(code, response);
    }

    private FirestoreResponse firestorePatch(String url, JSONObject body, String... updateMaskFields) throws IOException {
        ensureFreshFirebaseAuthToken(false);
        FirestoreResponse response = firestorePatchOnce(url, body, updateMaskFields);
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED || response.code == 401) {
            ensureFreshFirebaseAuthToken(true);
            response = firestorePatchOnce(url, body, updateMaskFields);
        }
        return response;
    }

    private FirestoreResponse firestorePatchOnce(String url, JSONObject body, String... updateMaskFields) throws IOException {
        StringBuilder patchedUrl = new StringBuilder(url);
        for (String field : updateMaskFields) {
            patchedUrl.append("&updateMask.fieldPaths=").append(field);
        }
        HttpURLConnection connection = (HttpURLConnection) new URL(patchedUrl.toString()).openConnection();
        connection.setConnectTimeout(12000);
        connection.setReadTimeout(12000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("User-Agent", "MetaFoldDownloader/" + currentAppVersion());
        setFirebaseAuthHeader(connection);
        try (OutputStream output = connection.getOutputStream()) {
            output.write(body.toString().getBytes(StandardCharsets.UTF_8));
        }
        int code = connection.getResponseCode();
        String response = readHttpBody(code >= 200 && code < 300 ? connection.getInputStream() : connection.getErrorStream());
        return new FirestoreResponse(code, response);
    }

    private static void putFirestoreString(JSONObject fields, String key, String value) throws Exception {
        JSONObject field = new JSONObject();
        field.put("stringValue", value == null ? "" : value);
        fields.put(key, field);
    }

    private static void putFirestoreTimestamp(JSONObject fields, String key, long millis) throws Exception {
        JSONObject field = new JSONObject();
        field.put("timestampValue", isoTimestamp(millis));
        fields.put(key, field);
    }

    private static String firestoreString(JSONObject fields, String key) {
        if (fields == null || TextUtils.isEmpty(key)) {
            return "";
        }
        JSONObject value = fields.optJSONObject(key);
        if (value == null) {
            return "";
        }
        return value.optString("stringValue", "");
    }

    private static String firestoreStringOrTimestamp(JSONObject fields, String key) {
        if (fields == null || TextUtils.isEmpty(key)) {
            return "";
        }
        JSONObject value = fields.optJSONObject(key);
        if (value == null) {
            return "";
        }
        String stringValue = value.optString("stringValue", "");
        if (!TextUtils.isEmpty(stringValue)) {
            return stringValue;
        }
        return value.optString("timestampValue", "");
    }

    private static long firestoreTimestampMillis(JSONObject fields, String key) {
        if (fields == null || TextUtils.isEmpty(key)) {
            return 0L;
        }
        JSONObject value = fields.optJSONObject(key);
        if (value == null) {
            return 0L;
        }
        return parseFirestoreTimestamp(value.optString("timestampValue", ""));
    }

    private static String isoTimestamp(long millis) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(new Date(millis));
    }

    private static long parseFirestoreTimestamp(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0L;
        }
        try {
            return Long.parseLong(value);
        } catch (Exception ignored) {
            // Try formatted timestamps next.
        }
        String[] patterns = new String[]{
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "yyyy-MM-dd'T'HH:mm:ss'Z'"
        };
        for (String pattern : patterns) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = format.parse(value);
                if (date != null) {
                    return date.getTime();
                }
            } catch (Exception ignored) {
                // Try the next timestamp format.
            }
        }
        return 0L;
    }

    private static long licenseExpiryMillis(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0L;
        }
        long timestamp = parseFirestoreTimestamp(value);
        if (timestamp > 0L) {
            return timestamp;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date = format.parse(value.trim());
            if (date != null) {
                return date.getTime() + (24L * 60L * 60L * 1000L) - 1L;
            }
        } catch (Exception ignored) {
            // Unknown date format means no local expiry enforcement.
        }
        return 0L;
    }

    private static boolean isLicenseExpired(String expiresAt) {
        long expiry = licenseExpiryMillis(expiresAt);
        return expiry > 0L && System.currentTimeMillis() > expiry;
    }

    private String formatDateTime(long millis) {
        if (millis <= 0L) {
            return "-";
        }
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault()).format(new Date(millis));
    }

    private boolean ensureUsageApproved() {
        if (!ensureNoMandatoryUpdate()) {
            return false;
        }
        if (!isSignedIn()) {
            showAuthRequiredDialog();
            return false;
        }
        if (isLicenseActive()) {
            if (needsLicenseRefresh()) {
                refreshLicenseSilently(false);
            }
            return true;
        }
        showLicenseRequiredDialog();
        return false;
    }

    private void requireFreshLicenseForUse(Runnable approvedAction) {
        if (!LICENSE_REQUIRED) {
            approvedAction.run();
            return;
        }
        if (!ensureNoMandatoryUpdate()) {
            return;
        }
        if (!isSignedIn()) {
            showAuthRequiredDialog();
            return;
        }
        if (!isLicenseActive()) {
            showLicenseRequiredDialog();
            return;
        }
        if (!isFirestoreLicenseConfigured()) {
            showFirestoreSetupDialog();
            return;
        }
        String email = getString(PREF_LICENSE_EMAIL, "").trim();
        if (TextUtils.isEmpty(email)) {
            email = currentAuthEmail();
        }
        if (TextUtils.isEmpty(email)) {
            showLicenseRequiredDialog();
            return;
        }
        final String checkedEmail = email;
        setBusy(true, "Lisans doğrulanıyor...");
        licenseExecutor.execute(() -> {
            try {
                LicenseResult result = requestLicenseValidation("", checkedEmail);
                mainHandler.post(() -> {
                    setBusy(false, null);
                    applyLicenseResult(result);
                    if (settingsPanel != null && settingsPanel.getVisibility() == View.VISIBLE) {
                        renderLicenseSettings(settingsPanel);
                    }
                    if (result.active && !isLicenseExpired(result.expiresAt)) {
                        approvedAction.run();
                    } else {
                        setStatus(result.message, false);
                        toast(result.message);
                        showLicenseRequiredDialog();
                    }
                });
            } catch (Exception error) {
                mainHandler.post(() -> {
                    setBusy(false, null);
                    String message = cleanError(error);
                    outputView.setText(message);
                    setStatus("Lisans doğrulanamadı", false);
                    new AlertDialog.Builder(this)
                            .setTitle(ui("Lisans doğrulanamadı"))
                            .setMessage(ui(message))
                            .setPositiveButton(ui("Onay durumunu kontrol et"), (dialog, which) -> openLicenseSettings())
                            .setNegativeButton(ui("Tamam"), null)
                            .show();
                });
            }
        });
    }

    private boolean isLicenseActive() {
        return !LICENSE_REQUIRED || (LICENSE_STATUS_ACTIVE.equals(getString(PREF_LICENSE_STATUS, "")) && !isLicenseExpired(getString(PREF_LICENSE_EXPIRES_AT, "")));
    }

    private void showLicenseRequiredDialog() {
        new AlertDialog.Builder(this)
                .setTitle(ui("Onay gerekli"))
                .setMessage(ui("Bu özelliği kullanmak için e-posta ile kayıt olunmalı ve yönetici onayı verilmelidir."))
                .setPositiveButton(ui("Onay durumunu kontrol et"), (dialog, which) -> {
                    openLicenseSettings();
                    validateLicense(false, settingsPanel);
                })
                .setNeutralButton(ui("Lisans ekranı"), (dialog, which) -> openLicenseSettings())
                .setNegativeButton(ui("Vazgeç"), null)
                .show();
    }

    private void openLicenseSettings() {
        hidePanels();
        if (appHeaderView != null) {
            appHeaderView.setVisibility(View.VISIBLE);
        }
        settingsPanel.setVisibility(View.VISIBLE);
        renderLicenseSettings(settingsPanel);
    }

    private String deviceId() {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return TextUtils.isEmpty(androidId) ? "unknown-device" : androidId;
    }

    private String shortDeviceId() {
        String id = deviceId();
        if (id.length() <= 8) {
            return id;
        }
        return "..." + id.substring(id.length() - 8);
    }

    private static String normalizeLicenseKey(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().replace(" ", "").toUpperCase(Locale.US);
    }

    private static String sha256Hex(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest((value == null ? "" : value).getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder(bytes.length * 2);
            for (byte current : bytes) {
                builder.append(String.format(Locale.US, "%02x", current & 0xff));
            }
            return builder.toString();
        } catch (Exception ignored) {
            return String.valueOf(Math.abs((value == null ? "" : value).hashCode()));
        }
    }

    private static String normalizeEmail(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.US);
    }

    private static boolean isValidEmail(String value) {
        return !TextUtils.isEmpty(value)
                && value.length() <= 254
                && value.contains("@")
                && value.indexOf('@') > 0
                && value.lastIndexOf('.') > value.indexOf('@') + 1;
    }

    private static String readHttpBody(InputStream stream) throws IOException {
        if (stream == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
        }
        return builder.toString().trim();
    }

    private void fetchFormatOptions(boolean fromBrowser) {
        requireFreshLicenseForUse(() -> fetchFormatOptionsAfterLicense(fromBrowser));
    }

    private void fetchFormatOptionsAfterLicense(boolean fromBrowser) {
        String normalizedUrl = normalizeUrl(urlInput.getText().toString().trim());
        String platform = detectPlatform(normalizedUrl);
        if (TextUtils.isEmpty(normalizedUrl)) {
            setStatus("Video linki gerekli.", false);
            return;
        }
        if (platform == null) {
            setStatus("Desteklenen platformlar: YouTube, Instagram, Facebook, TikTok, Pinterest, X.", false);
            return;
        }

        currentInfoUrl = "";
        currentInfoPlaylistMode = false;
        selectedOption = null;
        optionList.removeAllViews();
        optionButtons.clear();
        setFileActionsEnabled(false);
        platformView.setText(platform);
        SocialPlatform detectedPlatform = platformByDetectedName(platform);
        if (detectedPlatform != null) {
            activePlatform = detectedPlatform;
            updatePlatformHeader();
        }
        optionHeaderView.setText(ui("Seçenekler hazırlanıyor..."));
        outputView.setText("");

        boolean playlistMode = isPlaylistModeEnabled(normalizedUrl);
        if (playlistMode) {
            showPlaylistFormatOptions(normalizedUrl, buildPlaylistDownloadOptions(), fromBrowser);
            return;
        }
        showFastFormatOptions(normalizedUrl, buildPlaylistDownloadOptions(), fromBrowser);
    }

    private List<DownloadOption> buildDownloadOptions(VideoInfo info) {
        TreeSet<Integer> heights = new TreeSet<>(Collections.reverseOrder());
        boolean showHighResolution = getBool(PREF_SHOW_HIGH_RESOLUTION, true);
        if (info.getHeight() > 0) {
            if (showHighResolution || info.getHeight() <= 1080) {
                heights.add(info.getHeight());
            }
        }
        if (info.getFormats() != null) {
            for (VideoFormat format : info.getFormats()) {
                if (hasVideo(format) && format.getHeight() > 0) {
                    if (showHighResolution || format.getHeight() <= 1080) {
                        heights.add(format.getHeight());
                    }
                }
            }
        }

        List<DownloadOption> options = new ArrayList<>();
        if (heights.isEmpty()) {
            options.add(DownloadOption.video("En iyi video", 0));
        } else {
            for (Integer height : heights) {
                options.add(DownloadOption.video(labelForHeight(height), height));
            }
        }
        String audioFormat = getString(PREF_AUDIO_FORMAT, "MP3");
        options.add(DownloadOption.audio(audioFormat + " ses"));
        return options;
    }

    private List<DownloadOption> buildPlaylistDownloadOptions() {
        List<DownloadOption> options = new ArrayList<>();
        boolean showHighResolution = getBool(PREF_SHOW_HIGH_RESOLUTION, true);
        options.add(DownloadOption.video("En iyi video", 0));
        if (showHighResolution) {
            options.add(DownloadOption.video("4K (2160p)", 2160));
            options.add(DownloadOption.video("2K (1440p)", 1440));
        }
        options.add(DownloadOption.video("1080p", 1080));
        options.add(DownloadOption.video("720p", 720));
        options.add(DownloadOption.video("480p", 480));
        options.add(DownloadOption.video("360p", 360));
        String audioFormat = getString(PREF_AUDIO_FORMAT, "MP3");
        options.add(DownloadOption.audio(audioFormat + " ses"));
        return options;
    }

    private void showPlaylistFormatOptions(String url, List<DownloadOption> options, boolean fromBrowser) {
        currentInfoUrl = url;
        currentInfoPlaylistMode = true;
        optionList.removeAllViews();
        optionButtons.clear();

        optionHeaderView.setText(ui("Playlist modu: listedeki tüm videolar seçilen formatla indirilecek.\nİndirme türü seçin:"));
        for (DownloadOption option : options) {
            Button button = secondaryButton(option.label);
            button.setTag(option);
            button.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            button.setMinHeight(dp(46));
            button.setOnClickListener(v -> selectOption(option));
            LinearLayout.LayoutParams params = matchWrap();
            params.topMargin = dp(6);
            optionList.addView(button, params);
            optionButtons.add(button);
        }

        if (!options.isEmpty()) {
            selectOption(preferredOption(options));
        }
        setStatus(fromBrowser ? "Playlist sayfası hazır." : "Playlist seçenekleri hazır.", true);
    }

    private void showFastFormatOptions(String url, List<DownloadOption> options, boolean fromBrowser) {
        currentInfoUrl = url;
        currentInfoPlaylistMode = false;
        optionList.removeAllViews();
        optionButtons.clear();

        optionHeaderView.setText(ui("Hızlı mod: kaliteyi seçin, indirme sırasında en uygun format alınacak."));
        for (DownloadOption option : options) {
            Button button = secondaryButton(option.label);
            button.setTag(option);
            button.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            button.setMinHeight(dp(46));
            button.setOnClickListener(v -> selectOption(option));
            LinearLayout.LayoutParams params = matchWrap();
            params.topMargin = dp(6);
            optionList.addView(button, params);
            optionButtons.add(button);
        }

        if (!options.isEmpty()) {
            selectOption(preferredOption(options));
        }
        setStatus(fromBrowser ? "Oturumlu sayfa hazır." : "Hızlı seçenekler hazır.", true);
    }

    private void showFormatOptions(String url, VideoInfo info, List<DownloadOption> options, boolean fromBrowser) {
        currentInfoUrl = url;
        currentInfoPlaylistMode = false;
        optionList.removeAllViews();
        optionButtons.clear();

        String title = firstNonEmpty(info.getTitle(), info.getFulltitle(), "Video");
        int maxHeight = maxHeight(options);
        String maxLabel = maxHeight > 0 ? labelForHeight(maxHeight) : "bilinmiyor";
        optionHeaderView.setText(ui("Başlık: " + trimForUi(title, 90) + "\nMaksimum kalite: " + maxLabel + "\nİndirme türü seçin:"));

        for (DownloadOption option : options) {
            Button button = secondaryButton(option.label);
            button.setTag(option);
            button.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            button.setMinHeight(dp(46));
            button.setOnClickListener(v -> selectOption(option));
            LinearLayout.LayoutParams params = matchWrap();
            params.topMargin = dp(6);
            optionList.addView(button, params);
            optionButtons.add(button);
        }

        if (!options.isEmpty()) {
            selectOption(preferredOption(options));
        }
        setStatus(fromBrowser ? "Oturumlu sayfa hazır." : "Seçenekler hazır.", true);
    }

    private DownloadOption preferredOption(List<DownloadOption> options) {
        String preference = getString(PREF_DEFAULT_RESOLUTION, "720p");
        int targetHeight = resolutionHeight(preference);
        if ("En iyi".equals(preference) || targetHeight <= 0) {
            return options.get(0);
        }

        DownloadOption fallback = options.get(0);
        for (DownloadOption option : options) {
            if (!option.audio && option.height <= targetHeight) {
                return option;
            }
        }
        return fallback;
    }

    private void selectOption(DownloadOption option) {
        selectedOption = option;
        AppThemeOption theme = currentTheme();
        for (int i = 0; i < optionButtons.size(); i++) {
            Button button = optionButtons.get(i);
            boolean selected = button.getTag() == option;
            if (selected) {
                button.setTextColor(readableOn(activePlatform.accentColor));
                button.setBackground(rounded(activePlatform.accentColor, 8, activePlatform.accentColor));
            } else {
                button.setTextColor(theme.textColor);
                button.setBackground(rounded(theme.surfaceAltColor, 8, theme.borderColor));
            }
        }
    }

    private void startDownload() {
        requireFreshLicenseForUse(this::startDownloadAfterLicense);
    }

    private void startDownloadAfterLicense() {
        String normalizedUrl = normalizeUrl(urlInput.getText().toString().trim());
        String platform = detectPlatform(normalizedUrl);

        if (TextUtils.isEmpty(normalizedUrl)) {
            setStatus("Video linki gerekli.", false);
            return;
        }
        if (platform == null) {
            setStatus("Desteklenen platformlar: YouTube, Instagram, Facebook, TikTok, Pinterest, X.", false);
            return;
        }
        boolean playlistMode = isPlaylistModeEnabled(normalizedUrl);
        if (selectedOption == null || !normalizedUrl.equals(currentInfoUrl) || playlistMode != currentInfoPlaylistMode) {
            setStatus("Önce indirme seçeneklerini getirip kalite seçin.", false);
            return;
        }
        if (selectedOption.audio && !ffmpegReady) {
            setStatus("MP3 için FFmpeg hazır değil.", false);
            return;
        }
        DownloadOption option = selectedOption;
        if (downloading) {
            if (getBool(PREF_LIMIT_QUEUE, true)) {
                downloadQueue.addLast(new QueuedDownload(normalizedUrl, platform, option, playlistMode));
                setStatus("Kuyruğa eklendi. Bekleyen: " + downloadQueue.size(), true);
            } else {
                toast("İndirme zaten çalışıyor");
            }
            return;
        }
        startDownloadNow(normalizedUrl, platform, option, playlistMode, false);
    }

    private void startDownloadNow(String normalizedUrl, String platform, DownloadOption option, boolean playlistMode, boolean fromQueue) {
        SocialPlatform detectedPlatform = platformByDetectedName(platform);
        if (detectedPlatform != null) {
            activePlatform = detectedPlatform;
            updatePlatformHeader();
        }
        platformView.setText(platform + " - " + option.label);
        cancelRequested = false;
        setDownloading(true);
        progressBar.setProgress(0);
        progressBar.setIndeterminate(true);
        setStatus(fromQueue ? "Kuyruktaki indirme başlatılıyor..." : (playlistMode ? "Playlist listesi alınıyor..." : "İndirme başlatılıyor..."), true);
        outputView.setText("");
        activePlaylistDownload = playlistMode;
        resetPlaylistStatus(playlistMode);
        if (playlistMode && playlistStatusTitle != null) {
            playlistStatusTitle.setText(ui("Playlist durumu") + " - " + ui("Liste alınıyor..."));
        }
        hideLastDownloadSummary();
        lastFile = null;
        lastUri = null;
        setFileActionsEnabled(false);

        executor.execute(() -> {
            File jobDir = null;
            try {
                ensureDownloaderReady();

                jobDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "jobs/" + System.currentTimeMillis());
                if (!jobDir.mkdirs() && !jobDir.isDirectory()) {
                    throw new IOException("İndirme klasörü oluşturulamadı: " + jobDir.getAbsolutePath());
                }
                writeJobMetadata(jobDir, platform, normalizedUrl, option);

                File cookiesFile = buildCookieFile(normalizedUrl, jobDir);
                if (playlistMode) {
                    mainHandler.post(() -> setStatus("Playlist listesi alınıyor...", true));
                    List<PlaylistEntry> entries = fetchPlaylistEntriesForDisplay(normalizedUrl, cookiesFile);
                    if (entries.isEmpty()) {
                        throw new IOException("Playlist listesi alınamadı. Bağlantıyı kontrol edip tekrar deneyin.");
                    }
                    mainHandler.post(() -> showPlaylistEntries(entries));
                    PlaylistDownloadResult playlistResult = downloadPlaylistEntriesSequential(entries, normalizedUrl, option, jobDir, cookiesFile);
                    if (playlistResult.downloadedFiles.isEmpty()) {
                        throw new IOException("Playlist indirilemedi. Hiç dosya tamamlanmadı.");
                    }

                    File downloaded = playlistResult.downloadedFiles.get(0);
                    lastFile = downloaded;
                    lastUri = playlistResult.firstUri;
                    lastMimeType = guessMimeType(downloaded);

                    mainHandler.post(() -> {
                        activePlaylistDownload = false;
                        setDownloading(false);
                        progressBar.setIndeterminate(false);
                        progressBar.setProgress(100);
                        updatePlaylistCounter(playlistResult.successCount, playlistResult.totalCount, playlistResult.failedCount);
                        setStatus("Playlist tamamlandı.", playlistResult.failedCount == 0);
                        setFileActionsEnabled(true);
                        markPendingPlaylistRows("Atlandı", currentTheme().mutedColor);
                        outputView.setText(ui("Seçim: " + option.label
                                + "\nPlaylist: " + playlistResult.successCount + "/" + playlistResult.totalCount + " indirildi"
                                + (playlistResult.failedCount > 0 ? "\nHata: " + playlistResult.failedCount : "")
                                + "\nKlasör: " + outputFolderLabel(option.audio)));
                        showLastDownloadSummary(platform, option, downloaded);
                        if (downloadsPanel != null && downloadsPanel.getVisibility() == View.VISIBLE) {
                            refreshDownloads();
                        }
                        startNextQueuedDownload();
                    });
                    return;
                }
                List<File> downloadedFiles = tryDownloadInstagramPublicDirect(platform, normalizedUrl, jobDir, option);
                if (downloadedFiles.isEmpty()) {
                    YoutubeDLRequest request = buildDownloadRequest(normalizedUrl, jobDir, option, cookiesFile, playlistMode);
                    YoutubeDL.getInstance().execute(request, PROCESS_ID, progressCallback);
                    downloadedFiles = findDownloadedFiles(jobDir);
                }
                if (downloadedFiles.isEmpty()) {
                    throw new IOException("İndirilen dosya bulunamadı.");
                }

                File downloaded = downloadedFiles.get(0);
                Uri publicUri = null;
                for (File file : downloadedFiles) {
                    Uri copied = copyToPublicDownloads(file, option.audio);
                    if (publicUri == null) {
                        publicUri = copied;
                    }
                }
                String mimeType = guessMimeType(downloaded);

                lastFile = downloaded;
                lastUri = publicUri;
                lastMimeType = mimeType;
                final int downloadedFileCount = downloadedFiles.size();

                mainHandler.post(() -> {
                    activePlaylistDownload = false;
                    setDownloading(false);
                    progressBar.setIndeterminate(false);
                    progressBar.setProgress(100);
                    setStatus("İndirme tamamlandı.", true);
                    setFileActionsEnabled(true);
                    if (playlistMode) {
                        markPendingPlaylistRows("Atlandı", currentTheme().mutedColor);
                        outputView.setText(ui("Seçim: " + option.label + "\nPlaylist dosya sayısı: " + downloadedFileCount + "\nKlasör: " + outputFolderLabel(option.audio)));
                    } else {
                        outputView.setText(ui("Seçim: " + option.label + "\nDosya: " + outputFolderLabel(option.audio) + "/" + downloaded.getName()));
                    }
                    showLastDownloadSummary(platform, option, downloaded);
                    if (downloadsPanel != null && downloadsPanel.getVisibility() == View.VISIBLE) {
                        refreshDownloads();
                    }
                    startNextQueuedDownload();
                });
            } catch (Exception error) {
                String rawError = rawErrorText(error);
                boolean platformLoginError = !cancelRequested && isPlatformLoginError(platform, rawError);
                boolean instagramPublicError = !cancelRequested && isInstagramPublicExtractorError(platform, rawError);
                String displayError = cancelRequested
                        ? "İndirme iptal edildi."
                        : (platformLoginError
                        ? platformLoginMessage(platform)
                        : (instagramPublicError ? instagramPublicExtractorMessage() : cleanError(error)));
                if (playlistMode && jobDir != null && !cancelRequested) {
                    try {
                        List<File> partialFiles = findDownloadedFiles(jobDir);
                        if (!partialFiles.isEmpty()) {
                            File downloaded = partialFiles.get(0);
                            Uri publicUri = null;
                            for (File file : partialFiles) {
                                Uri copied = copyToPublicDownloads(file, option.audio);
                                if (publicUri == null) {
                                    publicUri = copied;
                                }
                            }
                            String mimeType = guessMimeType(downloaded);
                            lastFile = downloaded;
                            lastUri = publicUri;
                            lastMimeType = mimeType;
                            final int partialCount = partialFiles.size();
                            final String partialError = displayError;
                            mainHandler.post(() -> {
                                activePlaylistDownload = false;
                                setDownloading(false);
                                progressBar.setIndeterminate(false);
                                progressBar.setProgress(100);
                                setStatus("Playlist kısmen tamamlandı.", true);
                                setFileActionsEnabled(true);
                                markPendingPlaylistRows("Hata", Color.rgb(173, 52, 52));
                                outputView.setText(ui("Seçim: " + option.label
                                        + "\nİndirilen dosya sayısı: " + partialCount
                                        + "\nKlasör: " + outputFolderLabel(option.audio)
                                        + "\nUyarı: " + partialError));
                                showLastDownloadSummary(platform, option, downloaded);
                                if (downloadsPanel != null && downloadsPanel.getVisibility() == View.VISIBLE) {
                                    refreshDownloads();
                                }
                                startNextQueuedDownload();
                            });
                            return;
                        }
                    } catch (Exception copyError) {
                        displayError += "\n\nKısmi dosyalar kaydedilemedi: " + cleanError(copyError);
                    }
                }
                final String errorMessage = displayError;
                final boolean shouldShowPlatformLoginDialog = platformLoginError;
                mainHandler.post(() -> {
                    activePlaylistDownload = false;
                    setDownloading(false);
                    progressBar.setIndeterminate(false);
                    setStatus("İndirme başarısız.", false);
                    setFileActionsEnabled(false);
                    markPendingPlaylistRows("Hata", Color.rgb(173, 52, 52));
                    outputView.setText(ui(errorMessage));
                    if (shouldShowPlatformLoginDialog) {
                        showPlatformLoginRequiredDialog(platform, normalizedUrl);
                    }
                    startNextQueuedDownload();
                });
            }
        });
    }

    private void startNextQueuedDownload() {
        if (downloading || downloadQueue.isEmpty()) {
            return;
        }
        requireFreshLicenseForUse(() -> {
            QueuedDownload next = downloadQueue.pollFirst();
            if (next == null) {
                return;
            }
            currentInfoUrl = next.url;
            selectedOption = next.option;
            urlInput.setText(displayUrlForUi(next.url));
            urlInput.setSelection(urlInput.length());
            if (playlistSwitch != null) {
                playlistSwitch.setChecked(next.playlistMode);
            }
            currentInfoPlaylistMode = next.playlistMode;
            startDownloadNow(next.url, next.platform, next.option, next.playlistMode, true);
        });
    }

    private YoutubeDLRequest buildDownloadRequest(String url, File jobDir, DownloadOption option, File cookiesFile, boolean playlistMode) {
        return buildDownloadRequest(url, jobDir, option, cookiesFile, playlistMode, null);
    }

    private YoutubeDLRequest buildDownloadRequest(String url, File jobDir, DownloadOption option, File cookiesFile, boolean playlistMode, String outputTemplateOverride) {
        YoutubeDLRequest request = new YoutubeDLRequest(url);
        request.addOption("--no-mtime");
        request.addOption(playlistMode ? "--yes-playlist" : "--no-playlist");
        if (playlistMode) {
            request.addOption("--ignore-errors");
            request.addOption("--playlist-items", "1:");
            request.addOption("--skip-playlist-after-errors", "999999");
            request.addOption("--lazy-playlist");
        }
        request.addOption("--no-warnings");
        request.addOption("--socket-timeout", playlistMode ? "10" : "20");
        request.addOption("--concurrent-fragments", playlistMode ? "8" : "4");
        request.addOption("--fragment-retries", playlistMode ? "2" : "5");
        request.addOption("--file-access-retries", "2");
        request.addOption("--extractor-retries", playlistMode ? "1" : "5");
        request.addOption("--retries", playlistMode ? "1" : getString(PREF_MAX_RETRIES, "10"));
        String filenamePolicy = getString(PREF_FILENAME_CHARS, "Çoğu özel karakterler");
        if (!"Cogu ozel karakterler".equals(filenamePolicy) && !"Çoğu özel karakterler".equals(filenamePolicy)) {
            request.addOption("--restrict-filenames");
        }
        request.addOption("--trim-filenames", "120");
        String outputTemplate = outputTemplateOverride != null
                ? outputTemplateOverride
                : (playlistMode ? "%(playlist_index)s-%(title)s-%(id)s.%(ext)s" : "%(title)s-%(id)s.%(ext)s");
        request.addOption("-o", new File(jobDir, outputTemplate).getAbsolutePath());
        if (cookiesFile != null) {
            request.addOption("--cookies", cookiesFile.getAbsolutePath());
        }
        applyPlatformRequestHints(request, url);

        if (option.audio) {
            String audioFormat = getString(PREF_AUDIO_FORMAT, "MP3").toLowerCase(Locale.US);
            if ("m4a".equals(audioFormat)) {
                audioFormat = "m4a";
            } else if ("opus".equals(audioFormat)) {
                audioFormat = "opus";
            } else {
                audioFormat = "mp3";
            }
            request.addOption("-f", "bestaudio/best");
            request.addOption("-x");
            request.addOption("--audio-format", audioFormat);
            request.addOption("--audio-quality", "0");
            return request;
        }

        String videoFormat = "WebM".equals(getString(PREF_VIDEO_FORMAT, "MPEG-4")) ? "webm" : "mp4";
        if (ffmpegReady) {
            request.addOption("--merge-output-format", videoFormat);
            if (option.height > 0) {
                request.addOption("-f", videoFormatFilter(option.height, videoFormat));
            } else {
                request.addOption("-f", "bestvideo+bestaudio/best");
            }
        } else if (option.height > 0) {
            request.addOption("-f", "best[height<=" + option.height + "]/best");
        } else {
            request.addOption("-f", "best");
        }
        return request;
    }

    private void applyPlatformRequestHints(YoutubeDLRequest request, String url) {
        if (request == null || TextUtils.isEmpty(url)) {
            return;
        }
        String platform = detectPlatform(normalizeUrl(url));
        if ("Instagram".equals(platform)) {
            request.addOption("--add-header", "Referer:https://www.instagram.com/");
            request.addOption("--add-header", "Origin:https://www.instagram.com");
            request.addOption("--add-header", "User-Agent:" + mobileBrowserUserAgent());
        }
    }

    private static String mobileBrowserUserAgent() {
        return "Mozilla/5.0 (Linux; Android 13; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0 Mobile Safari/537.36";
    }

    private List<File> tryDownloadInstagramPublicDirect(String platform, String normalizedUrl, File jobDir, DownloadOption option) throws Exception {
        if (!"Instagram".equals(platform) || jobDir == null || option == null) {
            return Collections.emptyList();
        }
        try {
            mainHandler.post(() -> setStatus("Instagram public medya aranıyor...", true));
            InstagramPublicMedia media = fetchInstagramPublicMedia(normalizedUrl);
            if (media == null || TextUtils.isEmpty(media.videoUrl)) {
                return Collections.emptyList();
            }
            if (option.audio) {
                mainHandler.post(() -> setStatus("Instagram public medya ses olarak hazırlanıyor...", true));
                YoutubeDLRequest request = buildDirectMediaDownloadRequest(media, jobDir, option);
                YoutubeDL.getInstance().execute(request, PROCESS_ID, progressCallback);
                return findDownloadedFiles(jobDir);
            }

            File file = downloadDirectInstagramVideo(media, jobDir);
            return file == null || !file.isFile() || file.length() <= 0L
                    ? Collections.emptyList()
                    : Collections.singletonList(file);
        } catch (Exception error) {
            if (cancelRequested) {
                throw error;
            }
            return Collections.emptyList();
        }
    }

    private YoutubeDLRequest buildDirectMediaDownloadRequest(InstagramPublicMedia media, File jobDir, DownloadOption option) {
        YoutubeDLRequest request = new YoutubeDLRequest(media.videoUrl);
        request.addOption("--no-mtime");
        request.addOption("--no-warnings");
        request.addOption("--socket-timeout", "20");
        request.addOption("--retries", getString(PREF_MAX_RETRIES, "10"));
        request.addOption("--add-header", "Referer:" + firstNonEmpty(media.referer, "https://www.instagram.com/"));
        request.addOption("--add-header", "Origin:https://www.instagram.com");
        request.addOption("--add-header", "User-Agent:" + mobileBrowserUserAgent());
        String baseName = safeOutputBaseName(media.title, "Instagram-" + firstNonEmpty(media.shortcode, "video"));
        request.addOption("-o", new File(jobDir, baseName + ".%(ext)s").getAbsolutePath());
        if (option.audio) {
            String audioFormat = getString(PREF_AUDIO_FORMAT, "MP3").toLowerCase(Locale.US);
            if (!"m4a".equals(audioFormat) && !"opus".equals(audioFormat)) {
                audioFormat = "mp3";
            }
            request.addOption("-x");
            request.addOption("--audio-format", audioFormat);
            request.addOption("--audio-quality", "0");
        }
        return request;
    }

    private InstagramPublicMedia fetchInstagramPublicMedia(String normalizedUrl) throws IOException {
        InstagramPublicMedia graphQlMedia = fetchInstagramGraphQlMedia(normalizedUrl);
        if (graphQlMedia != null && !TextUtils.isEmpty(graphQlMedia.videoUrl)) {
            return graphQlMedia;
        }

        List<String> candidates = instagramPublicCandidateUrls(normalizedUrl);
        for (String candidate : candidates) {
            HttpURLConnection connection = null;
            try {
                connection = openInstagramConnection(candidate, "https://www.instagram.com/", false);
                int code = connection.getResponseCode();
                String body = readHttpBody(code >= 200 && code < 300 ? connection.getInputStream() : connection.getErrorStream());
                if (code >= 200 && code < 300) {
                    InstagramPublicMedia media = parseInstagramPublicMedia(body, candidate, normalizedUrl);
                    if (media != null && !TextUtils.isEmpty(media.videoUrl)) {
                        return media;
                    }
                }
            } catch (IOException ignored) {
                // Try the next public Instagram surface before falling back to yt-dlp.
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        return null;
    }

    private InstagramPublicMedia fetchInstagramGraphQlMedia(String normalizedUrl) throws IOException {
        String shortcode = instagramShortcode(normalizedUrl);
        String mediaId = instagramMediaIdFromShortcode(shortcode);
        if (TextUtils.isEmpty(shortcode) || TextUtils.isEmpty(mediaId)) {
            return null;
        }

        String referer = canonicalInstagramPermalink(normalizedUrl);
        InstagramWebSession session = bootstrapInstagramWebSession(referer);
        if (session == null || TextUtils.isEmpty(session.lsd)) {
            return null;
        }
        requestInstagramRuling(session, mediaId, referer);

        String variables = "{\"media_id\":\"" + mediaId + "\"}";
        String body = "av=0"
                + "&__d=www"
                + "&__user=0"
                + "&dpr=1"
                + "&lsd=" + urlEncode(session.lsd)
                + "&fb_api_caller_class=RelayModern"
                + "&fb_api_req_friendly_name=PolarisLoggedOutDesktopWWWPostRootContentQuery"
                + "&server_timestamps=true"
                + "&variables=" + urlEncode(variables)
                + "&doc_id=27130156389949648";

        HttpURLConnection connection = null;
        try {
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            connection = (HttpURLConnection) new URL("https://www.instagram.com/api/graphql").openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(25000);
            applyInstagramGraphQlHeaders(connection, session, referer);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setFixedLengthStreamingMode(bytes.length);
            try (OutputStream output = connection.getOutputStream()) {
                output.write(bytes);
            }
            int code = connection.getResponseCode();
            absorbSetCookies(connection, session.cookies);
            String response = readHttpBody(code >= 200 && code < 300 ? connection.getInputStream() : connection.getErrorStream());
            if (code < 200 || code >= 300 || TextUtils.isEmpty(response) || !response.trim().startsWith("{")) {
                return null;
            }
            JSONObject root = new JSONObject(response);
            String videoUrl = cleanExtractedUrl(firstVideoUrlInJson(root));
            if (TextUtils.isEmpty(videoUrl)) {
                return null;
            }
            String title = cleanExtractedText(firstCaptionTextInJson(root));
            if (TextUtils.isEmpty(title)) {
                title = "Instagram video";
            }
            return new InstagramPublicMedia(videoUrl, title, referer, shortcode);
        } catch (Exception ignored) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private InstagramWebSession bootstrapInstagramWebSession(String referer) throws IOException {
        Map<String, String> cookies = instagramInitialCookies();
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("https://www.instagram.com/").openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(20000);
            connection.setRequestProperty("User-Agent", mobileBrowserUserAgent());
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.setRequestProperty("Accept-Language", "tr-TR,tr;q=0.9,en-US;q=0.8,en;q=0.7");
            connection.setRequestProperty("Referer", firstNonEmpty(referer, "https://www.instagram.com/"));
            String cookieHeader = cookieHeader(cookies);
            if (!TextUtils.isEmpty(cookieHeader)) {
                connection.setRequestProperty("Cookie", cookieHeader);
            }
            int code = connection.getResponseCode();
            absorbSetCookies(connection, cookies);
            String body = readHttpBody(code >= 200 && code < 300 ? connection.getInputStream() : connection.getErrorStream());
            return new InstagramWebSession(cookies, findInstagramLsdToken(body));
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void requestInstagramRuling(InstagramWebSession session, String mediaId, String referer) {
        HttpURLConnection connection = null;
        try {
            String url = "https://i.instagram.com/api/v1/web/get_ruling_for_content/?content_type=MEDIA&target_id=" + urlEncode(mediaId);
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(12000);
            connection.setReadTimeout(15000);
            connection.setRequestProperty("User-Agent", mobileBrowserUserAgent());
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Accept-Language", "tr-TR,tr;q=0.9,en-US;q=0.8,en;q=0.7");
            connection.setRequestProperty("Origin", "https://www.instagram.com");
            connection.setRequestProperty("Referer", firstNonEmpty(referer, "https://www.instagram.com/"));
            connection.setRequestProperty("X-IG-App-ID", "936619743392459");
            connection.setRequestProperty("X-ASBD-ID", "359341");
            connection.setRequestProperty("X-IG-WWW-Claim", "0");
            String csrf = firstNonEmpty(session.cookies.get("csrftoken"), "");
            if (!TextUtils.isEmpty(csrf)) {
                connection.setRequestProperty("X-CSRFToken", csrf);
            }
            String cookieHeader = cookieHeader(session.cookies);
            if (!TextUtils.isEmpty(cookieHeader)) {
                connection.setRequestProperty("Cookie", cookieHeader);
            }
            connection.getResponseCode();
            absorbSetCookies(connection, session.cookies);
        } catch (Exception ignored) {
            // The GraphQL request can still work if this warm-up endpoint is unavailable.
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void applyInstagramGraphQlHeaders(HttpURLConnection connection, InstagramWebSession session, String referer) {
        connection.setRequestProperty("User-Agent", mobileBrowserUserAgent());
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Accept-Language", "tr-TR,tr;q=0.9,en-US;q=0.8,en;q=0.7");
        connection.setRequestProperty("Origin", "https://www.instagram.com");
        connection.setRequestProperty("Referer", firstNonEmpty(referer, "https://www.instagram.com/"));
        connection.setRequestProperty("X-IG-App-ID", "936619743392459");
        connection.setRequestProperty("X-ASBD-ID", "359341");
        connection.setRequestProperty("X-IG-WWW-Claim", "0");
        connection.setRequestProperty("X-FB-Friendly-Name", "PolarisLoggedOutDesktopWWWPostRootContentQuery");
        connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        connection.setRequestProperty("Sec-Fetch-Site", "same-origin");
        connection.setRequestProperty("Sec-Fetch-Mode", "cors");
        connection.setRequestProperty("Sec-Fetch-Dest", "empty");
        String csrf = firstNonEmpty(session.cookies.get("csrftoken"), "");
        if (!TextUtils.isEmpty(csrf)) {
            connection.setRequestProperty("X-CSRFToken", csrf);
        }
        if (!TextUtils.isEmpty(session.lsd)) {
            connection.setRequestProperty("X-FB-LSD", session.lsd);
        }
        String cookieHeader = cookieHeader(session.cookies);
        if (!TextUtils.isEmpty(cookieHeader)) {
            connection.setRequestProperty("Cookie", cookieHeader);
        }
    }

    private Map<String, String> instagramInitialCookies() {
        Map<String, String> cookies = new LinkedHashMap<>();
        try {
            flushWebCookies();
            parseCookieHeader(CookieManager.getInstance().getCookie("https://www.instagram.com/"), cookies);
            parseCookieHeader(CookieManager.getInstance().getCookie("https://instagram.com/"), cookies);
        } catch (Exception ignored) {
            // Fresh public cookies will be collected from instagram.com below.
        }
        return cookies;
    }

    private static void absorbSetCookies(HttpURLConnection connection, Map<String, String> cookies) {
        if (connection == null || cookies == null) {
            return;
        }
        Map<String, List<String>> headers = connection.getHeaderFields();
        if (headers == null) {
            return;
        }
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if (entry.getKey() == null || !"set-cookie".equalsIgnoreCase(entry.getKey()) || entry.getValue() == null) {
                continue;
            }
            for (String value : entry.getValue()) {
                parseCookieHeader(value, cookies);
            }
        }
    }

    private static void parseCookieHeader(String raw, Map<String, String> cookies) {
        if (TextUtils.isEmpty(raw) || cookies == null) {
            return;
        }
        String[] parts = raw.split(";");
        for (String part : parts) {
            String trimmed = part == null ? "" : part.trim();
            int equals = trimmed.indexOf('=');
            if (equals <= 0) {
                continue;
            }
            String name = trimmed.substring(0, equals).trim();
            String value = trimmed.substring(equals + 1).trim();
            if (TextUtils.isEmpty(name)
                    || "path".equalsIgnoreCase(name)
                    || "domain".equalsIgnoreCase(name)
                    || "expires".equalsIgnoreCase(name)
                    || "max-age".equalsIgnoreCase(name)
                    || "samesite".equalsIgnoreCase(name)) {
                continue;
            }
            cookies.put(name, value);
            break;
        }
    }

    private static String cookieHeader(Map<String, String> cookies) {
        if (cookies == null || cookies.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            if (TextUtils.isEmpty(entry.getKey()) || TextUtils.isEmpty(entry.getValue())) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append("; ");
            }
            builder.append(entry.getKey()).append('=').append(entry.getValue());
        }
        return builder.toString();
    }

    private static String findInstagramLsdToken(String html) {
        if (TextUtils.isEmpty(html)) {
            return "";
        }
        Matcher matcher = Pattern.compile("\\[\\\"LSD\\\",\\[\\],\\{\\\"token\\\":\\\"([^\\\"]+)\\\"", Pattern.CASE_INSENSITIVE).matcher(html);
        if (matcher.find()) {
            return decodeJsonEscapes(matcher.group(1));
        }
        matcher = Pattern.compile("\\\"lsd\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"", Pattern.CASE_INSENSITIVE).matcher(html);
        return matcher.find() ? decodeJsonEscapes(matcher.group(1)) : "";
    }

    private static String instagramMediaIdFromShortcode(String shortcode) {
        if (TextUtils.isEmpty(shortcode)) {
            return "";
        }
        final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
        BigInteger value = BigInteger.ZERO;
        for (int i = 0; i < shortcode.length(); i++) {
            int index = alphabet.indexOf(shortcode.charAt(i));
            if (index < 0) {
                return "";
            }
            value = value.multiply(BigInteger.valueOf(64L)).add(BigInteger.valueOf(index));
        }
        return value.toString();
    }

    private static String firstVideoUrlInJson(Object node) {
        if (node instanceof JSONObject) {
            JSONObject object = (JSONObject) node;
            JSONArray versions = object.optJSONArray("video_versions");
            String versionUrl = firstVideoVersionUrl(versions);
            if (!TextUtils.isEmpty(versionUrl)) {
                return versionUrl;
            }
            String direct = firstNonEmpty(
                    object.optString("video_url", ""),
                    object.optString("playable_url", ""),
                    object.optString("contentUrl", "")
            );
            if (!TextUtils.isEmpty(direct)) {
                return direct;
            }
            for (java.util.Iterator<String> iterator = object.keys(); iterator.hasNext(); ) {
                Object child = object.opt(iterator.next());
                String nested = firstVideoUrlInJson(child);
                if (!TextUtils.isEmpty(nested)) {
                    return nested;
                }
            }
        } else if (node instanceof JSONArray) {
            JSONArray array = (JSONArray) node;
            for (int i = 0; i < array.length(); i++) {
                String nested = firstVideoUrlInJson(array.opt(i));
                if (!TextUtils.isEmpty(nested)) {
                    return nested;
                }
            }
        }
        return "";
    }

    private static String firstVideoVersionUrl(JSONArray versions) {
        if (versions == null || versions.length() == 0) {
            return "";
        }
        String fallback = "";
        int bestArea = -1;
        for (int i = 0; i < versions.length(); i++) {
            JSONObject version = versions.optJSONObject(i);
            if (version == null) {
                continue;
            }
            String url = version.optString("url", "");
            if (TextUtils.isEmpty(url)) {
                continue;
            }
            int width = version.optInt("width", 0);
            int height = version.optInt("height", 0);
            int area = width * height;
            if (TextUtils.isEmpty(fallback) || area > bestArea) {
                fallback = url;
                bestArea = area;
            }
        }
        return fallback;
    }

    private static String firstCaptionTextInJson(Object node) {
        if (node instanceof JSONObject) {
            JSONObject object = (JSONObject) node;
            JSONObject caption = object.optJSONObject("caption");
            if (caption != null && !TextUtils.isEmpty(caption.optString("text", ""))) {
                return caption.optString("text", "");
            }
            String accessibility = object.optString("accessibility_caption", "");
            if (!TextUtils.isEmpty(accessibility)) {
                return accessibility;
            }
            for (java.util.Iterator<String> iterator = object.keys(); iterator.hasNext(); ) {
                String nested = firstCaptionTextInJson(object.opt(iterator.next()));
                if (!TextUtils.isEmpty(nested)) {
                    return nested;
                }
            }
        } else if (node instanceof JSONArray) {
            JSONArray array = (JSONArray) node;
            for (int i = 0; i < array.length(); i++) {
                String nested = firstCaptionTextInJson(array.opt(i));
                if (!TextUtils.isEmpty(nested)) {
                    return nested;
                }
            }
        }
        return "";
    }

    private static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8.name());
        } catch (Exception ignored) {
            return value == null ? "" : value;
        }
    }

    private List<String> instagramPublicCandidateUrls(String normalizedUrl) {
        List<String> urls = new ArrayList<>();
        String canonical = canonicalInstagramPermalink(normalizedUrl);
        addUnique(urls, canonical);
        addUnique(urls, normalizedUrl);
        String shortcode = instagramShortcode(canonical);
        if (!TextUtils.isEmpty(shortcode)) {
            String kind = instagramPermalinkKind(canonical);
            if (TextUtils.isEmpty(kind)) {
                kind = "reel";
            }
            String permalink = "https://www.instagram.com/" + kind + "/" + shortcode + "/";
            addUnique(urls, permalink);
            addUnique(urls, permalink + "embed/");
            addUnique(urls, permalink + "?__a=1&__d=dis");
            if (!"p".equals(kind)) {
                addUnique(urls, "https://www.instagram.com/p/" + shortcode + "/");
            }
        }
        return urls;
    }

    private String canonicalInstagramPermalink(String url) {
        String normalized = normalizeUrl(url);
        String shortcode = instagramShortcode(normalized);
        if (TextUtils.isEmpty(shortcode)) {
            return normalized;
        }
        String kind = instagramPermalinkKind(normalized);
        if (TextUtils.isEmpty(kind)) {
            kind = "reel";
        }
        return "https://www.instagram.com/" + kind + "/" + shortcode + "/";
    }

    private static String instagramPermalinkKind(String url) {
        try {
            Uri uri = Uri.parse(url);
            List<String> segments = uri.getPathSegments();
            if (segments.isEmpty()) {
                return "";
            }
            String first = segments.get(0).toLowerCase(Locale.US);
            if ("reels".equals(first)) {
                return "reel";
            }
            if ("reel".equals(first) || "p".equals(first) || "tv".equals(first)) {
                return first;
            }
        } catch (Exception ignored) {
            // Keep the default fallback.
        }
        return "";
    }

    private static String instagramShortcode(String url) {
        try {
            Uri uri = Uri.parse(url);
            List<String> segments = uri.getPathSegments();
            if (segments.size() >= 2) {
                String first = segments.get(0).toLowerCase(Locale.US);
                if ("reel".equals(first) || "reels".equals(first) || "p".equals(first) || "tv".equals(first)) {
                    return segments.get(1);
                }
            }
        } catch (Exception ignored) {
            // Regex fallback below.
        }
        Matcher matcher = Pattern.compile("/(?:reel|reels|p|tv)/([^/?#]+)/?", Pattern.CASE_INSENSITIVE).matcher(url == null ? "" : url);
        return matcher.find() ? matcher.group(1) : "";
    }

    private InstagramPublicMedia parseInstagramPublicMedia(String body, String sourceUrl, String originalUrl) {
        if (TextUtils.isEmpty(body)) {
            return null;
        }
        String decodedBody = decodeBasicHtml(body);
        String videoUrl = firstNonEmpty(
                findMetaContent(decodedBody, "og:video"),
                findMetaContent(decodedBody, "og:video:secure_url"),
                findMetaContent(decodedBody, "twitter:player:stream"),
                findJsonStringValue(body, "video_url"),
                findJsonStringValue(decodedBody, "video_url"),
                findJsonStringValue(body, "playable_url"),
                findJsonStringValue(decodedBody, "playable_url"),
                findJsonStringValue(body, "contentUrl"),
                findJsonStringValue(decodedBody, "contentUrl")
        );
        videoUrl = cleanExtractedUrl(videoUrl);
        if (TextUtils.isEmpty(videoUrl) || !videoUrl.startsWith("http")) {
            return null;
        }

        String title = cleanExtractedText(firstNonEmpty(
                findMetaContent(decodedBody, "og:title"),
                findMetaContent(decodedBody, "twitter:title"),
                findJsonStringValue(decodedBody, "title"),
                "Instagram video"
        ));
        String shortcode = instagramShortcode(firstNonEmpty(originalUrl, sourceUrl));
        String referer = canonicalInstagramPermalink(firstNonEmpty(originalUrl, sourceUrl));
        return new InstagramPublicMedia(videoUrl, title, referer, shortcode);
    }

    private static String findMetaContent(String html, String key) {
        if (TextUtils.isEmpty(html) || TextUtils.isEmpty(key)) {
            return "";
        }
        Pattern tagPattern = Pattern.compile("<meta\\s+[^>]*(?:property|name)\\s*=\\s*([\"'])" + Pattern.quote(key) + "\\1[^>]*>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher tagMatcher = tagPattern.matcher(html);
        while (tagMatcher.find()) {
            String content = extractAttribute(tagMatcher.group(), "content");
            if (!TextUtils.isEmpty(content)) {
                return content;
            }
        }
        return "";
    }

    private static String extractAttribute(String tag, String attribute) {
        Matcher matcher = Pattern.compile("\\b" + Pattern.quote(attribute) + "\\s*=\\s*([\"'])(.*?)\\1", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(tag == null ? "" : tag);
        return matcher.find() ? matcher.group(2) : "";
    }

    private static String findJsonStringValue(String text, String key) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(key)) {
            return "";
        }
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"(.*?)\"", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String value = cleanExtractedUrl(matcher.group(1));
            if (!TextUtils.isEmpty(value)) {
                return value;
            }
        }
        return "";
    }

    private static String cleanExtractedUrl(String value) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        String cleaned = decodeJsonEscapes(decodeBasicHtml(value.trim()))
                .replace("\\/", "/")
                .replace("\\u0026", "&")
                .replace("\\u003d", "=")
                .replace("\\u003f", "?")
                .replace("\\u002f", "/");
        cleaned = decodeBasicHtml(cleaned).trim();
        if ((cleaned.startsWith("\"") && cleaned.endsWith("\""))
                || (cleaned.startsWith("'") && cleaned.endsWith("'"))) {
            cleaned = cleaned.substring(1, cleaned.length() - 1).trim();
        }
        return cleaned;
    }

    private static String cleanExtractedText(String value) {
        String cleaned = decodeJsonEscapes(decodeBasicHtml(value == null ? "" : value));
        cleaned = cleaned.replaceAll("<[^>]+>", " ").replaceAll("\\s+", " ").trim();
        return TextUtils.isEmpty(cleaned) ? "Instagram video" : cleaned;
    }

    private static String decodeBasicHtml(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&#34;", "\"")
                .replace("&#x22;", "\"")
                .replace("&#39;", "'")
                .replace("&#x27;", "'")
                .replace("&#47;", "/")
                .replace("&#x2F;", "/")
                .replace("&lt;", "<")
                .replace("&gt;", ">");
    }

    private static String decodeJsonEscapes(String value) {
        if (TextUtils.isEmpty(value) || value.indexOf('\\') < 0) {
            return value == null ? "" : value;
        }
        StringBuilder builder = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (current != '\\' || i + 1 >= value.length()) {
                builder.append(current);
                continue;
            }
            char next = value.charAt(++i);
            if (next == 'u' && i + 4 < value.length()) {
                String hex = value.substring(i + 1, i + 5);
                try {
                    builder.append((char) Integer.parseInt(hex, 16));
                    i += 4;
                    continue;
                } catch (NumberFormatException ignored) {
                    builder.append("\\u");
                    continue;
                }
            }
            switch (next) {
                case '/':
                case '\\':
                case '"':
                    builder.append(next);
                    break;
                case 'n':
                    builder.append('\n');
                    break;
                case 'r':
                    builder.append('\r');
                    break;
                case 't':
                    builder.append('\t');
                    break;
                default:
                    builder.append(next);
                    break;
            }
        }
        return builder.toString();
    }

    private File downloadDirectInstagramVideo(InstagramPublicMedia media, File jobDir) throws IOException {
        HttpURLConnection connection = openInstagramConnection(media.videoUrl, media.referer, true);
        try {
            int code = connection.getResponseCode();
            if (code < 200 || code >= 300) {
                throw new IOException("Instagram medya dosyası indirilemedi. HTTP " + code);
            }
            long total = connection.getContentLengthLong();
            String extension = mediaExtension(media.videoUrl, connection.getContentType());
            String baseName = safeOutputBaseName(media.title, "Instagram-" + firstNonEmpty(media.shortcode, "video"));
            File output = uniqueOutputFile(jobDir, baseName, extension);
            byte[] buffer = new byte[1024 * 64];
            long downloaded = 0L;
            int lastPercent = -1;
            mainHandler.post(() -> {
                progressBar.setIndeterminate(total <= 0L);
                setStatus("Instagram public medya indiriliyor...", true);
            });
            try (InputStream input = connection.getInputStream();
                 OutputStream outputStream = new FileOutputStream(output)) {
                int read;
                while ((read = input.read(buffer)) != -1) {
                    if (cancelRequested) {
                        throw new IOException("İndirme iptal edildi.");
                    }
                    outputStream.write(buffer, 0, read);
                    downloaded += read;
                    if (total > 0L) {
                        int percent = Math.min(100, (int) ((downloaded * 100L) / total));
                        if (percent != lastPercent) {
                            lastPercent = percent;
                            long currentDownloaded = downloaded;
                            mainHandler.post(() -> {
                                progressBar.setIndeterminate(false);
                                progressBar.setProgress(percent);
                                setStatus("Instagram public medya indiriliyor... %" + percent + " - " + formatBytes(currentDownloaded) + " / " + formatBytes(total), true);
                            });
                        }
                    }
                }
            }
            if (!output.isFile() || output.length() <= 0L) {
                throw new IOException("Instagram medya dosyası boş görünüyor.");
            }
            mainHandler.post(() -> {
                progressBar.setIndeterminate(false);
                progressBar.setProgress(100);
                setStatus("Instagram public medya indirildi.", true);
            });
            return output;
        } finally {
            connection.disconnect();
        }
    }

    private HttpURLConnection openInstagramConnection(String url, String referer, boolean media) throws IOException {
        String currentUrl = url;
        for (int redirect = 0; redirect < 6; redirect++) {
            HttpURLConnection connection = (HttpURLConnection) new URL(currentUrl).openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(media ? 45000 : 20000);
            connection.setRequestProperty("User-Agent", mobileBrowserUserAgent());
            connection.setRequestProperty("Accept-Language", "tr-TR,tr;q=0.9,en-US;q=0.8,en;q=0.7");
            connection.setRequestProperty("Referer", firstNonEmpty(referer, "https://www.instagram.com/"));
            connection.setRequestProperty("Origin", "https://www.instagram.com");
            connection.setRequestProperty("X-IG-App-ID", "936619743392459");
            connection.setRequestProperty("Accept", media
                    ? "video/mp4,video/*,*/*;q=0.8"
                    : "text/html,application/xhtml+xml,application/xml;q=0.9,application/json;q=0.8,*/*;q=0.7");
            String cookieHeader = instagramSessionCookieHeader();
            if (!TextUtils.isEmpty(cookieHeader)) {
                connection.setRequestProperty("Cookie", cookieHeader);
            }
            int code = connection.getResponseCode();
            if (code >= 300 && code < 400) {
                String location = connection.getHeaderField("Location");
                connection.disconnect();
                if (TextUtils.isEmpty(location)) {
                    throw new IOException("Instagram yönlendirmesi boş döndü.");
                }
                currentUrl = new URL(new URL(currentUrl), location).toString();
                continue;
            }
            return connection;
        }
        throw new IOException("Instagram yönlendirmesi çok fazla tekrarlandı.");
    }

    private String instagramSessionCookieHeader() {
        try {
            flushWebCookies();
            String raw = CookieManager.getInstance().getCookie("https://www.instagram.com/");
            if (raw != null && raw.toLowerCase(Locale.US).contains("sessionid=")) {
                return raw;
            }
        } catch (Exception ignored) {
            // Public downloads should continue without cookies.
        }
        return "";
    }

    private static String safeOutputBaseName(String title, String fallback) {
        String value = firstNonEmpty(title, fallback, "Instagram-video");
        value = value.replaceAll("[\\\\/:*?\"<>|\\r\\n\\t]+", " ")
                .replaceAll("\\s+", " ")
                .trim();
        if (TextUtils.isEmpty(value)) {
            value = "Instagram-video";
        }
        if (value.length() > 96) {
            value = value.substring(0, 96).trim();
        }
        return value;
    }

    private static File uniqueOutputFile(File directory, String baseName, String extension) {
        String safeExtension = TextUtils.isEmpty(extension) ? "mp4" : extension.toLowerCase(Locale.US);
        File output = new File(directory, baseName + "." + safeExtension);
        int index = 2;
        while (output.exists()) {
            output = new File(directory, baseName + "-" + index + "." + safeExtension);
            index++;
        }
        return output;
    }

    private static String mediaExtension(String url, String contentType) {
        String lowerType = contentType == null ? "" : contentType.toLowerCase(Locale.US);
        if (lowerType.contains("mp4")) {
            return "mp4";
        }
        if (lowerType.contains("webm")) {
            return "webm";
        }
        try {
            String path = new URI(url).getPath();
            int dot = path == null ? -1 : path.lastIndexOf('.');
            if (dot >= 0 && dot < path.length() - 1) {
                String extension = path.substring(dot + 1).toLowerCase(Locale.US);
                if (extension.matches("[a-z0-9]{2,5}")) {
                    return extension;
                }
            }
        } catch (Exception ignored) {
            // Keep mp4 as the safest Instagram default.
        }
        return "mp4";
    }

    private PlaylistDownloadResult downloadPlaylistEntriesSequential(List<PlaylistEntry> entries, String playlistUrl, DownloadOption option, File jobDir, File cookiesFile) throws Exception {
        List<File> downloadedFiles = new ArrayList<>();
        Set<String> copiedPaths = new HashSet<>();
        Uri firstUri = null;
        int successCount = 0;
        int failedCount = 0;
        int totalCount = entries == null ? 0 : entries.size();

        mainHandler.post(() -> updatePlaylistCounter(0, totalCount, 0));
        if (entries == null || entries.isEmpty()) {
            return new PlaylistDownloadResult(downloadedFiles, null, 0, 0, 0);
        }

        for (PlaylistEntry entry : entries) {
            if (cancelRequested) {
                throw new IOException("İndirme iptal edildi.");
            }

            String entryUrl = playlistEntryDownloadUrl(playlistUrl, entry);
            String key = keyForPlaylistIndex(entry.index);
            if (skippedPlaylistIndexes.contains(entry.index)) {
                mainHandler.post(() -> setPlaylistRowStatus(key, "Atlandı", currentTheme().mutedColor));
                continue;
            }

            int doneBefore = successCount;
            int failedBefore = failedCount;
            skipCurrentRequested = false;
            mainHandler.post(() -> {
                activePlaylistIndex = entry.index;
                activePlaylistKey = key;
                setPlaylistRowStatus(key, "İndiriliyor", activePlatform.accentColor);
                updatePlaylistCounter(doneBefore, totalCount, failedBefore);
            });

            if (TextUtils.isEmpty(entryUrl)) {
                failedCount++;
                int failedNow = failedCount;
                mainHandler.post(() -> {
                    setPlaylistRowStatus(key, "Hata", Color.rgb(173, 52, 52));
                    updatePlaylistCounter(doneBefore, totalCount, failedNow);
                });
                continue;
            }

            try {
                String outputTemplate = String.format(Locale.US, "%03d-", Math.max(1, entry.index)) + "%(title)s-%(id)s.%(ext)s";
                YoutubeDLRequest request = buildDownloadRequest(entryUrl, jobDir, option, cookiesFile, false, outputTemplate);
                YoutubeDL.getInstance().execute(request, PROCESS_ID, progressCallback);

                List<File> newFiles = findUncopiedDownloadedFiles(jobDir, copiedPaths);
                if (newFiles.isEmpty()) {
                    throw new IOException("Video dosyası tamamlanmadı.");
                }
                for (File file : newFiles) {
                    Uri copied = copyToPublicDownloads(file, option.audio);
                    if (firstUri == null) {
                        firstUri = copied;
                    }
                    copiedPaths.add(file.getAbsolutePath());
                    downloadedFiles.add(file);
                }

                successCount++;
                int successNow = successCount;
                int failedNow = failedCount;
                File playableFile = newFiles.get(0);
                playlistDownloadedFiles.put(entry.index, playableFile);
                mainHandler.post(() -> {
                    completedPlaylistCount = successNow;
                    failedPlaylistCount = failedNow;
                    setPlaylistRowStatus(key, "Tamamlandı", Color.rgb(31, 135, 86));
                    setPlaylistRowPlayable(key, playableFile);
                    updatePlaylistCounter(successNow, totalCount, failedNow);
                });
            } catch (Exception entryError) {
                if (cancelRequested) {
                    throw entryError;
                }
                if (skipCurrentRequested || skippedPlaylistIndexes.contains(entry.index)) {
                    skipCurrentRequested = false;
                    mainHandler.post(() -> setPlaylistRowStatus(key, "Atlandı", currentTheme().mutedColor));
                    continue;
                }
                failedCount++;
                int successNow = successCount;
                int failedNow = failedCount;
                mainHandler.post(() -> {
                    failedPlaylistCount = failedNow;
                    setPlaylistRowStatus(key, "Hata", Color.rgb(173, 52, 52));
                    updatePlaylistCounter(successNow, totalCount, failedNow);
                });
            }
        }

        return new PlaylistDownloadResult(downloadedFiles, firstUri, successCount, failedCount, totalCount);
    }

    private List<File> findUncopiedDownloadedFiles(File jobDir, Set<String> copiedPaths) {
        List<File> files = findDownloadedFiles(jobDir);
        List<File> fresh = new ArrayList<>();
        for (File file : files) {
            if (file != null && !copiedPaths.contains(file.getAbsolutePath())) {
                fresh.add(file);
            }
        }
        fresh.sort(Comparator.comparing(File::getName));
        return fresh;
    }

    private String playlistEntryDownloadUrl(String playlistUrl, PlaylistEntry entry) {
        String candidate = cleanPlaylistTitle(entry.url);
        if (!TextUtils.isEmpty(candidate) && !"NA".equalsIgnoreCase(candidate)) {
            String normalizedCandidate = normalizeUrl(candidate);
            if (detectPlatform(normalizedCandidate) != null) {
                return normalizedCandidate;
            }
        }

        String platform = detectPlatform(playlistUrl);
        if ("YouTube".equals(platform)) {
            String videoId = firstNonEmpty(cleanPlaylistTitle(entry.id), candidate);
            if (!TextUtils.isEmpty(videoId)
                    && !"NA".equalsIgnoreCase(videoId)
                    && !videoId.contains("/")
                    && !videoId.contains(":")) {
                return "https://www.youtube.com/watch?v=" + videoId;
            }
        }
        return "";
    }

    private void updatePlaylistCounter(int completed, int total, int failed) {
        activePlaylistTotal = Math.max(0, total);
        completedPlaylistCount = Math.max(0, completed);
        failedPlaylistCount = Math.max(0, failed);
        if (playlistStatusTitle == null) {
            return;
        }
        String title = "Playlist durumu - " + completedPlaylistCount + "/" + activePlaylistTotal + " indirildi";
        if (failedPlaylistCount > 0) {
            title += ", " + failedPlaylistCount + " hata";
        }
        playlistStatusTitle.setText(ui(title));
    }

    private List<PlaylistEntry> fetchPlaylistEntriesForDisplay(String url, File cookiesFile) {
        try {
            YoutubeDLRequest request = new YoutubeDLRequest(url);
            request.addOption("--flat-playlist");
            request.addOption("--playlist-items", "1:");
            request.addOption("--skip-download");
            request.addOption("--ignore-errors");
            request.addOption("--skip-playlist-after-errors", "999999");
            request.addOption("--no-warnings");
            request.addOption("--socket-timeout", "20");
            request.addOption("--extractor-retries", "3");
            request.addOption("--retries", "3");
            request.addOption("--print", PLAYLIST_ENTRY_PREFIX + "%(playlist_index)s\t%(title)s\t%(id)s\t%(url)s\t%(webpage_url)s");
            if (cookiesFile != null) {
                request.addOption("--cookies", cookiesFile.getAbsolutePath());
            }
            applyPlatformRequestHints(request, url);
            YoutubeDLResponse response = YoutubeDL.getInstance().execute(request, PLAYLIST_LIST_PROCESS_ID);
            return parsePlaylistEntries(response == null ? "" : response.getOut());
        } catch (Exception ignored) {
            return Collections.emptyList();
        }
    }

    private List<PlaylistEntry> parsePlaylistEntries(String output) {
        List<PlaylistEntry> entries = new ArrayList<>();
        if (TextUtils.isEmpty(output)) {
            return entries;
        }
        String[] lines = output.replace("\r\n", "\n").replace('\r', '\n').split("\n");
        Set<Integer> seenIndexes = new HashSet<>();
        for (String rawLine : lines) {
            String line = rawLine == null ? "" : rawLine.trim();
            int prefixIndex = line.indexOf(PLAYLIST_ENTRY_PREFIX);
            if (prefixIndex < 0) {
                continue;
            }
            String payload = line.substring(prefixIndex + PLAYLIST_ENTRY_PREFIX.length());
            String[] parts = payload.split("\t", 5);
            int index = parsePositiveInt(parts.length > 0 ? parts[0] : "", entries.size() + 1);
            if (seenIndexes.contains(index)) {
                continue;
            }
            String title = parts.length > 1 ? cleanPlaylistTitle(parts[1]) : "";
            String id = parts.length > 2 ? parts[2].trim() : "";
            String entryUrl = firstNonEmpty(parts.length > 3 ? parts[3].trim() : "", parts.length > 4 ? parts[4].trim() : "");
            if (TextUtils.isEmpty(title)) {
                title = "Video " + index;
            }
            entries.add(new PlaylistEntry(index, title, id, entryUrl));
            seenIndexes.add(index);
        }
        entries.sort(Comparator.comparingInt(entry -> entry.index));
        return entries;
    }

    private void resetPlaylistStatus(boolean visible) {
        activePlaylistIndex = 0;
        activePlaylistTotal = 0;
        completedPlaylistCount = 0;
        failedPlaylistCount = 0;
        activePlaylistKey = "";
        playlistStatusRows.clear();
        skippedPlaylistIndexes.clear();
        playlistDownloadedFiles.clear();
        if (playlistStatusList != null) {
            playlistStatusList.removeAllViews();
        }
        if (playlistStatusTitle != null) {
            playlistStatusTitle.setText(ui("Playlist durumu"));
        }
        if (playlistStatusPanel != null) {
            playlistStatusPanel.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private void showPlaylistEntries(List<PlaylistEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            if (playlistStatusTitle != null) {
                playlistStatusTitle.setText(ui("Playlist durumu") + " - " + ui("Liste alınamadı."));
            }
            return;
        }
        resetPlaylistStatus(true);
        activePlaylistTotal = entries.size();
        updatePlaylistCounter(0, entries.size(), 0);
        for (PlaylistEntry entry : entries) {
            upsertPlaylistStatusRow(keyForPlaylistIndex(entry.index), entry.index, entry.title, "Bekliyor", currentTheme().mutedColor);
        }
    }

    private void ensurePlaylistPlaceholders(int total) {
        if (total <= 0 || playlistStatusList == null) {
            return;
        }
        activePlaylistTotal = Math.max(activePlaylistTotal, total);
        if (playlistStatusTitle != null) {
            playlistStatusTitle.setText(ui("Playlist durumu") + " (" + activePlaylistTotal + ")");
        }
        for (int i = 1; i <= total; i++) {
            String key = keyForPlaylistIndex(i);
            if (!playlistStatusRows.containsKey(key)) {
                upsertPlaylistStatusRow(key, i, "Video " + i, "Bekliyor", currentTheme().mutedColor);
            }
        }
    }

    private void updateActivePlaylistProgress(int percent) {
        if (TextUtils.isEmpty(activePlaylistKey) || !playlistStatusRows.containsKey(activePlaylistKey)) {
            return;
        }
        if (skippedPlaylistIndexes.contains(activePlaylistIndex)) {
            return;
        }
        if (percent >= 100) {
            setPlaylistRowStatus(activePlaylistKey, "Tamamlandı", Color.rgb(31, 135, 86));
        } else if (percent > 0) {
            setPlaylistRowStatus(activePlaylistKey, "İndiriliyor %" + percent, activePlatform.accentColor);
        }
    }

    private void markPendingPlaylistRows(String status, int color) {
        for (Map.Entry<String, PlaylistStatusRow> entry : playlistStatusRows.entrySet()) {
            String current = String.valueOf(entry.getValue().statusView.getText());
            if (!current.contains("Tamamlandı") && !current.contains("Hata")) {
                setPlaylistRowStatus(entry.getKey(), status, color);
            }
        }
    }

    private void setPlaylistRowStatus(String key, String status, int color) {
        PlaylistStatusRow row = playlistStatusRows.get(key);
        if (row == null) {
            return;
        }
        row.statusView.setText(ui(status));
        row.statusView.setTextColor(color);
        applyPlaylistRowProgress(row, status, color);
        updatePlaylistRowControls(row, status);
    }

    private void updatePlaylistRowControls(PlaylistStatusRow row, String status) {
        if (row == null) {
            return;
        }
        String safeStatus = status == null ? "" : status;
        boolean complete = safeStatus.contains("Tamamlandı");
        boolean failed = safeStatus.contains("Hata");
        boolean skipped = safeStatus.contains("Atlandı");
        boolean active = safeStatus.startsWith("İndiriliyor");
        row.includeBox.setOnCheckedChangeListener(null);
        row.includeBox.setChecked(!skipped);
        row.includeBox.setEnabled(!complete && !failed);
        row.includeBox.setOnCheckedChangeListener((buttonView, checked) -> setPlaylistEntryIncluded(row.index, checked));
        if (complete && row.file != null) {
            row.actionButton.setText(ui("Oynat"));
            row.actionButton.setEnabled(true);
        } else if (skipped) {
            row.actionButton.setText(ui("Atlandı"));
            row.actionButton.setEnabled(false);
        } else {
            row.actionButton.setText(ui(active ? "Atla" : "Atla"));
            row.actionButton.setEnabled(!failed);
        }
    }

    private void setPlaylistRowPlayable(String key, File file) {
        PlaylistStatusRow row = playlistStatusRows.get(key);
        if (row == null) {
            return;
        }
        row.file = file;
        row.actionButton.setText(ui("Oynat"));
        row.actionButton.setEnabled(file != null);
    }

    private void setPlaylistEntryIncluded(int index, boolean included) {
        String key = keyForPlaylistIndex(index);
        PlaylistStatusRow row = playlistStatusRows.get(key);
        if (included) {
            skippedPlaylistIndexes.remove(index);
            if (row != null && row.file == null && index != activePlaylistIndex) {
                setPlaylistRowStatus(key, "Bekliyor", currentTheme().mutedColor);
            }
            return;
        }
        skipPlaylistEntry(index);
    }

    private void skipPlaylistEntry(int index) {
        skippedPlaylistIndexes.add(index);
        String key = keyForPlaylistIndex(index);
        setPlaylistRowStatus(key, "Atlandı", currentTheme().mutedColor);
        if (activePlaylistDownload && index == activePlaylistIndex) {
            skipCurrentRequested = true;
            new Thread(() -> {
                try {
                    YoutubeDL.getInstance().destroyProcessById(PROCESS_ID);
                } catch (Exception ignored) {
                    // Process may already be between videos.
                }
            }, "metafold-skip-video").start();
        }
    }

    private void applyPlaylistRowProgress(PlaylistStatusRow row, String status, int color) {
        if (row.progressBar == null) {
            return;
        }
        String safeStatus = status == null ? "" : status;
        int progress = progressFromStatus(safeStatus);
        boolean active = safeStatus.startsWith("İndiriliyor");
        boolean complete = safeStatus.contains("Tamamlandı");
        boolean failed = safeStatus.contains("Hata");
        boolean skipped = safeStatus.contains("Atlandı");

        int tint = color;
        if (complete) {
            progress = 100;
            tint = Color.rgb(31, 135, 86);
        } else if (failed) {
            progress = Math.max(progress, 100);
            tint = Color.rgb(173, 52, 52);
        } else if (skipped) {
            progress = 100;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            row.progressBar.setProgressTintList(ColorStateList.valueOf(tint));
            row.progressBar.setProgressBackgroundTintList(ColorStateList.valueOf(currentTheme().borderColor));
            row.progressBar.setIndeterminateTintList(ColorStateList.valueOf(tint));
        }

        if (active && progress <= 0) {
            row.progressBar.setIndeterminate(true);
            row.progressBar.setAlpha(1f);
        } else {
            row.progressBar.setIndeterminate(false);
            row.progressBar.setProgress(Math.max(0, Math.min(100, progress)));
            row.progressBar.setAlpha((complete || failed || skipped || progress > 0) ? 1f : 0.45f);
        }

        row.container.animate()
                .scaleX(active ? 1.01f : 1f)
                .scaleY(active ? 1.01f : 1f)
                .setDuration(180L)
                .start();
    }

    private static int progressFromStatus(String status) {
        if (TextUtils.isEmpty(status)) {
            return 0;
        }
        Matcher matcher = Pattern.compile("%\\s*(\\d{1,3})").matcher(status);
        if (matcher.find()) {
            return Math.max(0, Math.min(100, parsePositiveInt(matcher.group(1), 0)));
        }
        return 0;
    }

    private void upsertPlaylistStatusRow(String key, int index, String title, String status, int statusColor) {
        if (playlistStatusList == null || TextUtils.isEmpty(key)) {
            return;
        }
        if (playlistStatusPanel != null && playlistStatusPanel.getVisibility() != View.VISIBLE) {
            playlistStatusPanel.setVisibility(View.VISIBLE);
        }
        PlaylistStatusRow row = playlistStatusRows.get(key);
        if (row == null) {
            row = createPlaylistStatusRow(index, title);
            playlistStatusRows.put(key, row);
            LinearLayout.LayoutParams params = matchWrap();
            params.topMargin = dp(6);
            row.container.setAlpha(0f);
            playlistStatusList.addView(row.container, params);
            row.container.animate().alpha(1f).setDuration(180L).start();
        }
        if (!TextUtils.isEmpty(title)) {
            String currentTitle = String.valueOf(row.titleView.getText());
            boolean currentIsPlaceholder = currentTitle.startsWith("Video ");
            boolean nextIsPlaceholder = title.startsWith("Video ");
            if (currentIsPlaceholder || !nextIsPlaceholder) {
                row.titleView.setText(trimForUi(title, 96));
            }
        }
        setPlaylistRowStatus(key, status, statusColor);
    }

    private PlaylistStatusRow createPlaylistStatusRow(int index, String title) {
        AppThemeOption theme = currentTheme();
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(10), dp(8), dp(10), dp(8));
        row.setBackground(rounded(theme.surfaceAltColor, 8, theme.borderColor));

        CheckBox includeBox = new CheckBox(this);
        includeBox.setChecked(true);
        includeBox.setOnCheckedChangeListener((buttonView, checked) -> setPlaylistEntryIncluded(index, checked));
        row.addView(includeBox, new LinearLayout.LayoutParams(dp(38), dp(42)));

        TextView number = textView(String.valueOf(index), 12, readableOn(activePlatform.accentColor), true);
        number.setGravity(Gravity.CENTER);
        number.setBackground(rounded(activePlatform.accentColor, 18, activePlatform.accentColor));
        LinearLayout.LayoutParams numberParams = new LinearLayout.LayoutParams(dp(34), dp(34));
        numberParams.leftMargin = dp(4);
        row.addView(number, numberParams);

        LinearLayout texts = new LinearLayout(this);
        texts.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        textParams.leftMargin = dp(10);
        row.addView(texts, textParams);

        TextView titleView = textView(TextUtils.isEmpty(title) ? "Video " + index : trimForUi(title, 96), 13, theme.textColor, true);
        titleView.setSingleLine(true);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        texts.addView(titleView);

        TextView status = textView("Bekliyor", 12, theme.mutedColor, false);
        status.setPadding(0, dp(2), 0, 0);
        texts.addView(status);

        ProgressBar rowProgress = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        rowProgress.setMax(100);
        rowProgress.setProgress(0);
        rowProgress.setAlpha(0.45f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rowProgress.setProgressTintList(ColorStateList.valueOf(activePlatform.accentColor));
            rowProgress.setProgressBackgroundTintList(ColorStateList.valueOf(theme.borderColor));
            rowProgress.setIndeterminateTintList(ColorStateList.valueOf(activePlatform.accentColor));
        }
        LinearLayout.LayoutParams progressParams = matchWrap();
        progressParams.topMargin = dp(6);
        texts.addView(rowProgress, progressParams);

        Button actionButton = secondaryButton("Atla");
        actionButton.setTextSize(11);
        actionButton.setMinHeight(dp(34));
        actionButton.setPadding(dp(6), 0, dp(6), 0);
        actionButton.setOnClickListener(v -> {
            PlaylistStatusRow current = playlistStatusRows.get(keyForPlaylistIndex(index));
            if (current != null && current.file != null) {
                openFile(current.file);
            } else {
                skipPlaylistEntry(index);
            }
        });
        LinearLayout.LayoutParams actionParams = new LinearLayout.LayoutParams(dp(70), dp(38));
        actionParams.leftMargin = dp(8);
        row.addView(actionButton, actionParams);

        return new PlaylistStatusRow(index, row, titleView, status, rowProgress, includeBox, actionButton);
    }

    private static String keyForPlaylistIndex(int index) {
        return "index:" + Math.max(1, index);
    }

    private static int parsePositiveInt(String value, int fallback) {
        if (TextUtils.isEmpty(value)) {
            return fallback;
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed > 0 ? parsed : fallback;
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    private static String cleanPlaylistTitle(String title) {
        if (title == null) {
            return "";
        }
        String cleaned = title.replace('\n', ' ').replace('\r', ' ').trim();
        if ("NA".equalsIgnoreCase(cleaned) || "null".equalsIgnoreCase(cleaned)) {
            return "";
        }
        return cleaned;
    }

    private static String displayNameFromPath(String path) {
        String cleaned = cleanPlaylistTitle(path);
        if (TextUtils.isEmpty(cleaned)) {
            return "";
        }
        int slash = Math.max(cleaned.lastIndexOf('/'), cleaned.lastIndexOf('\\'));
        if (slash >= 0 && slash + 1 < cleaned.length()) {
            cleaned = cleaned.substring(slash + 1);
        }
        int dot = cleaned.lastIndexOf('.');
        if (dot > 0) {
            cleaned = cleaned.substring(0, dot);
        }
        cleaned = cleaned.replaceFirst("^\\d{1,4}-", "");
        return cleanPlaylistTitle(cleaned);
    }

    private void cancelDownload() {
        if (!downloading) {
            return;
        }
        cancelRequested = true;
        setStatus("İptal ediliyor...", false);
        executor.execute(() -> {
            try {
                YoutubeDL.getInstance().destroyProcessById(PLAYLIST_LIST_PROCESS_ID);
                YoutubeDL.getInstance().destroyProcessById(PROCESS_ID);
            } catch (Exception ignored) {
                // No active process.
            }
        });
    }

    private void updateExtractor() {
        if (downloading) {
            toast("İndirme sırasında güncelleme yapılamaz");
            return;
        }
        setBusy(true, "İndirme motoru güncelleniyor...");
        executor.execute(() -> {
            try {
                ensureDownloaderReady();
                UpdateResult updateResult = updateYoutubeDl(true);
                mainHandler.post(() -> {
                    setBusy(false, null);
                    setStatus(updateResult.message, true);
                });
            } catch (Exception error) {
                mainHandler.post(() -> {
                    setBusy(false, null);
                    setStatus("Güncelleme başarısız.", false);
                    outputView.setText(cleanError(error));
                });
            }
        });
    }

    private void ensureDownloaderReady() throws YoutubeDLException {
        synchronized (initLock) {
            if (downloaderReady) {
                return;
            }
            YoutubeDL.getInstance().init(this);
            ffmpegReady = initOptionalLibrary(
                    "com.yausername.ffmpeg.FFmpeg",
                    "com.yausername.youtubedl_android.FFmpeg"
            );
            downloaderReady = true;
        }
    }

    private UpdateResult updateYoutubeDl(boolean force) throws YoutubeDLException {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        long now = System.currentTimeMillis();
        long lastUpdate = prefs.getLong(PREF_LAST_UPDATE, 0L);
        if (!force && lastUpdate > 0L && now - lastUpdate < UPDATE_INTERVAL_MS) {
            return new UpdateResult("Hazir");
        }

        mainHandler.post(() -> setStatus("İndirme motoru güncelleniyor...", true));
        YoutubeDL.UpdateStatus status = YoutubeDL.getInstance().updateYoutubeDL(this, YoutubeDL.UpdateChannel._STABLE);
        prefs.edit().putLong(PREF_LAST_UPDATE, now).apply();
        if (status == YoutubeDL.UpdateStatus.DONE) {
            return new UpdateResult("İndirme motoru güncellendi");
        }
        return new UpdateResult("Hazir");
    }

    private String safeVersionName() {
        try {
            String version = YoutubeDL.getInstance().versionName(this);
            if (!TextUtils.isEmpty(version)) {
                return "yt-dlp " + version.trim();
            }
        } catch (Exception ignored) {
            // Version is only informational.
        }
        return "yt-dlp";
    }

    private boolean initOptionalLibrary(String... classNames) {
        for (String className : classNames) {
            try {
                Class<?> libraryClass = Class.forName(className);
                Method getInstance = libraryClass.getMethod("getInstance");
                Object instance = getInstance.invoke(null);
                Method init = libraryClass.getMethod("init", Context.class);
                init.invoke(instance, this);
                return true;
            } catch (ClassNotFoundException ignored) {
                // Try the next package name.
            } catch (Exception ignored) {
                return false;
            }
        }
        return false;
    }

    private File buildCookieFile(String pageUrl, File parentDir) throws IOException {
        CookieManager manager = CookieManager.getInstance();
        flushWebCookies();
        List<String> lookupUrls = cookieLookupUrls(pageUrl);
        Map<String, CookieLine> cookies = new LinkedHashMap<>();
        for (String lookupUrl : lookupUrls) {
            String raw = manager.getCookie(lookupUrl);
            if (TextUtils.isEmpty(raw)) {
                continue;
            }
            String host = hostOf(lookupUrl);
            if (TextUtils.isEmpty(host)) {
                continue;
            }
            String domain = cookieDomain(host);
            String[] pairs = raw.split(";");
            for (String pair : pairs) {
                String trimmed = pair.trim();
                int equals = trimmed.indexOf('=');
                if (equals <= 0) {
                    continue;
                }
                String name = trimmed.substring(0, equals).trim();
                String value = trimmed.substring(equals + 1).trim();
                if (TextUtils.isEmpty(name)) {
                    continue;
                }
                String key = domain + "|" + name;
                cookies.put(key, new CookieLine(domain, name, value));
            }
        }

        if ("Instagram".equals(detectPlatform(normalizeUrl(pageUrl))) && !hasInstagramSessionCookie(cookies)) {
            cookies.values().removeIf(MainActivity::isInstagramCookie);
        }

        if (cookies.isEmpty()) {
            return null;
        }

        File file = new File(parentDir, "metafold-cookies-" + System.currentTimeMillis() + ".txt");
        long expires = System.currentTimeMillis() / 1000L + 365L * 24L * 60L * 60L;
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write("# Netscape HTTP Cookie File\n");
            for (CookieLine cookie : cookies.values()) {
                writer.write(cookie.domain);
                writer.write("\tTRUE\t/\tTRUE\t");
                writer.write(String.valueOf(expires));
                writer.write('\t');
                writer.write(cookie.name);
                writer.write('\t');
                writer.write(cookie.value);
                writer.write('\n');
            }
        }
        return file;
    }

    private static boolean hasInstagramSessionCookie(Map<String, CookieLine> cookies) {
        if (cookies == null || cookies.isEmpty()) {
            return false;
        }
        for (CookieLine cookie : cookies.values()) {
            if (isInstagramCookie(cookie)
                    && "sessionid".equalsIgnoreCase(cookie.name)
                    && !TextUtils.isEmpty(cookie.value)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInstagramCookie(CookieLine cookie) {
        return cookie != null
                && cookie.domain != null
                && cookie.domain.toLowerCase(Locale.US).contains("instagram.com");
    }

    private List<String> cookieLookupUrls(String pageUrl) {
        List<String> urls = new ArrayList<>();
        addUnique(urls, normalizeUrl(pageUrl));
        addUnique(urls, "https://www.youtube.com/");
        addUnique(urls, "https://m.youtube.com/");
        addUnique(urls, "https://accounts.google.com/");
        addUnique(urls, "https://www.google.com/");
        addUnique(urls, "https://instagram.com/");
        addUnique(urls, "https://www.instagram.com/");
        addUnique(urls, "https://m.instagram.com/");
        addUnique(urls, "https://www.instagram.com/accounts/login/");
        addUnique(urls, "https://www.facebook.com/");
        addUnique(urls, "https://m.facebook.com/");
        addUnique(urls, "https://www.tiktok.com/");
        addUnique(urls, "https://www.pinterest.com/");
        addUnique(urls, "https://pinterest.com/");
        addUnique(urls, "https://pin.it/");
        addUnique(urls, "https://x.com/");
        addUnique(urls, "https://twitter.com/");
        return urls;
    }

    private void flushWebCookies() {
        try {
            CookieManager.getInstance().flush();
        } catch (Exception ignored) {
            // Cookie flush is best-effort; downloads can still try without it.
        }
    }

    private Uri copyToPublicDownloads(File source, boolean audio) throws IOException {
        Uri selectedFolderUri = selectedFolderUri(audio);
        if (selectedFolderUri != null) {
            return copyToSelectedFolder(source, selectedFolderUri);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return FileProvider.getUriForFile(this, getPackageName() + ".files", source);
        }

        String mimeType = guessMimeType(source);
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, source.getName());
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/" + DOWNLOAD_FOLDER);
        values.put(MediaStore.MediaColumns.IS_PENDING, 1);

        Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
        if (uri == null) {
            throw new IOException("Downloads kaydi olusturulamadi.");
        }

        try (InputStream input = new FileInputStream(source);
             OutputStream output = getContentResolver().openOutputStream(uri)) {
            if (output == null) {
                throw new IOException("Downloads dosyası açılamadı.");
            }
            copy(input, output);
        } catch (IOException error) {
            getContentResolver().delete(uri, null, null);
            throw error;
        }

        ContentValues done = new ContentValues();
        done.put(MediaStore.MediaColumns.IS_PENDING, 0);
        getContentResolver().update(uri, done, null, null);
        return uri;
    }

    private Uri selectedFolderUri(boolean audio) {
        String value = getString(audio ? PREF_AUDIO_FOLDER_URI : PREF_VIDEO_FOLDER_URI, "");
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        try {
            return Uri.parse(value);
        } catch (Exception ignored) {
            return null;
        }
    }

    private String outputFolderLabel(boolean audio) {
        Uri selected = selectedFolderUri(audio);
        if (selected == null) {
            return "Downloads/" + DOWNLOAD_FOLDER;
        }
        return folderLabel(audio ? PREF_AUDIO_FOLDER_URI : PREF_VIDEO_FOLDER_URI);
    }

    private Uri copyToSelectedFolder(File source, Uri folderUri) throws IOException {
        DocumentFile folder = DocumentFile.fromTreeUri(this, folderUri);
        if (folder == null || !folder.isDirectory()) {
            throw new IOException("Seçili klasör açılamadı.");
        }
        DocumentFile target = folder.createFile(guessMimeType(source), source.getName());
        if (target == null || target.getUri() == null) {
            throw new IOException("Seçili klasörde dosya oluşturulamadı.");
        }
        try (InputStream input = new FileInputStream(source);
             OutputStream output = getContentResolver().openOutputStream(target.getUri())) {
            if (output == null) {
                throw new IOException("Seçili klasöre yazılamadı.");
            }
            copy(input, output);
            return target.getUri();
        }
    }

    private File findDownloadedFile(File directory) {
        List<File> files = findDownloadedFiles(directory);
        return files.isEmpty() ? null : files.get(0);
    }

    private List<File> findDownloadedFiles(File directory) {
        List<File> files = new ArrayList<>();
        collectCandidateFiles(directory, files);
        files.sort(Comparator
                .comparingLong(File::lastModified)
                .thenComparingLong(File::length)
                .reversed());
        return files;
    }

    private void collectCandidateFiles(File directory, List<File> files) {
        File[] children = directory.listFiles();
        if (children == null) {
            return;
        }
        for (File child : children) {
            if (child.isDirectory()) {
                collectCandidateFiles(child, files);
                continue;
            }
            String name = child.getName().toLowerCase(Locale.US);
            if (child.length() > 0
                    && !name.endsWith(".part")
                    && !name.endsWith(".ytdl")
                    && !name.endsWith(".temp")
                    && !name.endsWith(".tmp")
                    && !name.endsWith(".description")
                    && !name.endsWith(".json")
                    && !name.endsWith(".properties")
                    && !name.equals(JOB_METADATA_FILE)
                    && !name.contains("cookie")) {
                files.add(child);
            }
        }
    }

    private void openLastFile() {
        Uri uri = lastUri;
        if (uri == null && lastFile != null) {
            uri = FileProvider.getUriForFile(this, getPackageName() + ".files", lastFile);
        }
        if (uri == null) {
            toast("Önce bir video indirin");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, lastMimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startChooser(intent, "Dosyayı aç");
    }

    private void shareLastFile() {
        Uri uri = lastUri;
        if (uri == null && lastFile != null) {
            uri = FileProvider.getUriForFile(this, getPackageName() + ".files", lastFile);
        }
        if (uri == null) {
            toast("Önce bir video indirin");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(lastMimeType);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startChooser(intent, "Dosyayı paylaş");
    }

    private void openFile(File file) {
        if (file == null || !file.exists()) {
            toast("Dosya bulunamadı");
            return;
        }
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".files", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, guessMimeType(file));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startChooser(intent, "Dosyayı aç");
    }

    private void shareFile(File file) {
        if (file == null || !file.exists()) {
            toast("Dosya bulunamadı");
            return;
        }
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".files", file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(guessMimeType(file));
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startChooser(intent, "Dosyayı paylaş");
    }

    private void startChooser(Intent intent, String title) {
        try {
            startActivity(Intent.createChooser(intent, title));
        } catch (Exception error) {
            setStatus("Uygun uygulama bulunamadı.", false);
        }
    }

    private void pasteFromClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (clipboard == null || !clipboard.hasPrimaryClip()) {
            toast("Panoda link yok");
            return;
        }
        ClipData clip = clipboard.getPrimaryClip();
        if (clip == null || clip.getItemCount() == 0) {
            toast("Panoda link yok");
            return;
        }
        CharSequence text = clip.getItemAt(0).coerceToText(this);
        String url = extractFirstUrl(String.valueOf(text));
        if (TextUtils.isEmpty(url)) {
            url = String.valueOf(text).trim();
        }
        setUrlAndReset(url);
    }

    private boolean handleIncomingIntent(Intent intent) {
        if (intent == null) {
            return false;
        }

        String raw = "";
        if (Intent.ACTION_SEND.equals(intent.getAction())) {
            CharSequence shared = intent.getCharSequenceExtra(Intent.EXTRA_TEXT);
            raw = shared == null ? "" : shared.toString();
        } else if (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getData() != null) {
            raw = intent.getData().toString();
        }

        if (TextUtils.isEmpty(raw)) {
            return false;
        }

        String url = extractFirstUrl(raw);
        if (TextUtils.isEmpty(url)) {
            url = raw.trim();
        }
        if (detectPlatform(normalizeUrl(url)) == null) {
            return false;
        }
        setUrlAndReset(url);
        showDownloader();
        return true;
    }

    private void setUrlAndReset(String url) {
        urlInput.setText(displayUrlForUi(url));
        urlInput.setSelection(urlInput.length());
        currentInfoUrl = "";
        currentInfoPlaylistMode = false;
        selectedOption = null;
        optionList.removeAllViews();
        optionButtons.clear();
        optionHeaderView.setText(ui("Kalite seçenekleri henüz alınmadı."));
        outputView.setText("");
        hideLastDownloadSummary();
        updatePlaylistModeVisibility(url, true);
        showDetectedPlatform(url);
        SocialPlatform detected = platformByDetectedName(detectPlatform(normalizeUrl(url)));
        if (detected != null) {
            activePlatform = detected;
            updatePlatformHeader();
        }
    }

    private void clearSelection() {
        urlInput.setText("");
        platformView.setText(ui("Link modu"));
        optionHeaderView.setText(ui("Kalite seçenekleri henüz alınmadı."));
        optionList.removeAllViews();
        optionButtons.clear();
        outputView.setText("");
        hideLastDownloadSummary();
        currentInfoUrl = "";
        currentInfoPlaylistMode = false;
        selectedOption = null;
        updatePlaylistModeVisibility("", false);
    }

    private String extractFirstUrl(String text) {
        if (text == null) {
            return "";
        }
        Matcher matcher = URL_PATTERN.matcher(text);
        if (!matcher.find()) {
            return "";
        }
        return matcher.group(1).replaceAll("[),.;]+$", "");
    }

    private void showDetectedPlatform(String url) {
        String platform = detectPlatform(normalizeUrl(url));
        updatePlaylistModeVisibility(url, false);
        platformView.setText(platform == null ? ui("Desteklenmeyen link") : platform);
    }

    private void updatePlaylistModeVisibility(String url, boolean autoCheck) {
        if (playlistModeRow == null || playlistSwitch == null) {
            return;
        }
        boolean playlist = isPlaylistCandidate(normalizeUrl(url));
        playlistModeRow.setVisibility(playlist ? View.VISIBLE : View.GONE);
        playlistSwitch.setEnabled(playlist && !busy && !downloading);
        if (autoCheck) {
            playlistSwitch.setChecked(playlist);
        } else if (!playlist) {
            playlistSwitch.setChecked(false);
        }
    }

    private void resetInfoForPlaylistToggle() {
        if (optionHeaderView == null || optionList == null) {
            return;
        }
        currentInfoUrl = "";
        currentInfoPlaylistMode = false;
        selectedOption = null;
        optionList.removeAllViews();
        optionButtons.clear();
        optionHeaderView.setText(ui("Kalite seçenekleri henüz alınmadı."));
        setFileActionsEnabled(false);
    }

    private boolean isPlaylistModeEnabled(String normalizedUrl) {
        return playlistSwitch != null
                && playlistSwitch.isChecked()
                && isPlaylistCandidate(normalizedUrl);
    }

    private boolean isPlaylistCandidate(String normalizedUrl) {
        if (TextUtils.isEmpty(normalizedUrl)) {
            return false;
        }
        String platform = detectPlatform(normalizedUrl);
        if (!"YouTube".equals(platform)) {
            return false;
        }
        try {
            Uri uri = Uri.parse(normalizedUrl);
            String list = uri.getQueryParameter("list");
            return !TextUtils.isEmpty(list) || (uri.getPath() != null && uri.getPath().contains("/playlist"));
        } catch (Exception ignored) {
            return normalizedUrl.contains("list=");
        }
    }

    private String normalizeUrl(String rawUrl) {
        if (rawUrl == null) {
            return "";
        }
        String trimmed = displayUrlForUi(rawUrl).trim();
        if (trimmed.isEmpty()) {
            return "";
        }
        if (trimmed.startsWith("//")) {
            trimmed = "https:" + trimmed;
        } else if (!trimmed.toLowerCase(Locale.US).startsWith("http://")
                && !trimmed.toLowerCase(Locale.US).startsWith("https://")) {
            trimmed = "https://" + trimmed;
        }
        try {
            Uri uri = Uri.parse(trimmed);
            if (TextUtils.isEmpty(uri.getScheme()) || TextUtils.isEmpty(uri.getEncodedAuthority())) {
                return trimmed;
            }
            return new URI(
                    uri.getScheme(),
                    uri.getEncodedAuthority(),
                    normalizeInstagramPath(uri),
                    uri.getQuery(),
                    uri.getFragment()
            ).toASCIIString();
        } catch (Exception ignored) {
            return trimmed;
        }
    }

    private String normalizeInstagramPath(Uri uri) {
        String path = uri.getPath();
        String host = uri.getHost();
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(host)) {
            return path;
        }
        String normalizedHost = host.toLowerCase(Locale.US);
        if (normalizedHost.endsWith("instagram.com") && path.startsWith("/reels/")) {
            return "/reel/" + path.substring("/reels/".length());
        }
        return path;
    }

    private String displayUrlForUi(String rawUrl) {
        if (rawUrl == null) {
            return "";
        }
        try {
            return Uri.decode(rawUrl.trim());
        } catch (Exception ignored) {
            return rawUrl.trim();
        }
    }

    private String detectPlatform(String url) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            if (host == null) {
                return null;
            }
            host = host.toLowerCase(Locale.US);
            if (host.startsWith("www.")) {
                host = host.substring(4);
            }
            if (host.equals("youtu.be") || host.endsWith("youtube.com") || host.endsWith("youtube-nocookie.com")) {
                return "YouTube";
            }
            if (host.endsWith("instagram.com")) {
                return "Instagram";
            }
            if (host.equals("fb.watch") || host.endsWith("facebook.com")) {
                return "Facebook";
            }
            if (host.endsWith("tiktok.com")) {
                return "TikTok";
            }
            if (host.endsWith("pinterest.com") || host.equals("pin.it")) {
                return "Pinterest";
            }
            if (host.endsWith("x.com") || host.endsWith("twitter.com")) {
                return "X / Twitter";
            }
            return null;
        } catch (URISyntaxException error) {
            return null;
        }
    }

    private String guessMimeType(File file) {
        String extension = "";
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        if (dot >= 0 && dot + 1 < name.length()) {
            extension = name.substring(dot + 1).toLowerCase(Locale.US);
        }
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (!TextUtils.isEmpty(mimeType)) {
            return mimeType;
        }
        if ("mp4".equals(extension) || "m4v".equals(extension)) {
            return "video/mp4";
        }
        if ("webm".equals(extension)) {
            return "video/webm";
        }
        if ("mp3".equals(extension)) {
            return "audio/mpeg";
        }
        if ("m4a".equals(extension)) {
            return "audio/mp4";
        }
        return "application/octet-stream";
    }

    private void setDownloading(boolean active) {
        downloading = active;
        if (active) {
            startDownloadKeepAlive();
        } else {
            stopDownloadKeepAlive();
        }
        setControlsEnabled(!active && !busy);
        cancelButton.setEnabled(active);
        downloadButton.setEnabled(!busy && (!active || getBool(PREF_LIMIT_QUEUE, true)));
        if (active) {
            startProgressPulse();
        } else {
            stopProgressPulse();
        }
    }

    private void startDownloadKeepAlive() {
        try {
            Intent intent = new Intent(this, DownloadKeepAliveService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        } catch (Exception ignored) {
            // Downloads still run in-process; the service is an extra keep-alive layer.
        }
    }

    private void stopDownloadKeepAlive() {
        try {
            stopService(new Intent(this, DownloadKeepAliveService.class));
        } catch (Exception ignored) {
            // Service may not have been started.
        }
    }

    private void setBusy(boolean active, String status) {
        busy = active;
        progressBar.setIndeterminate(active);
        if (!active) {
            progressBar.setIndeterminate(false);
        }
        setControlsEnabled(!active && !downloading);
        if (!TextUtils.isEmpty(status)) {
            setStatus(status, true);
        }
    }

    private void setControlsEnabled(boolean enabled) {
        urlInput.setEnabled(enabled);
        pasteButton.setEnabled(enabled);
        analyzeButton.setEnabled(enabled);
        updateButton.setEnabled(enabled);
        if (playlistSwitch != null) {
            playlistSwitch.setEnabled(enabled && isPlaylistCandidate(normalizeUrl(urlInput.getText().toString())));
        }
        downloadButton.setEnabled(!busy && (enabled || (downloading && getBool(PREF_LIMIT_QUEUE, true))));
        browserDownloadButton.setEnabled(enabled && browserPageDownloadAvailable);
    }

    private void setFileActionsEnabled(boolean enabled) {
        openButton.setEnabled(enabled);
        shareButton.setEnabled(enabled);
    }

    private void showLastDownloadSummary(String platformName, DownloadOption option, File file) {
        if (lastDownloadSummary == null || file == null) {
            return;
        }
        SocialPlatform platform = platformByDetectedName(platformName);
        int accent = platform == null ? activePlatform.accentColor : platform.accentColor;
        AppThemeOption theme = currentTheme();

        lastDownloadSummary.removeAllViews();
        lastDownloadSummary.setOrientation(LinearLayout.HORIZONTAL);
        lastDownloadSummary.setGravity(Gravity.CENTER_VERTICAL);
        lastDownloadSummary.setPadding(dp(10), dp(10), dp(10), dp(10));
        lastDownloadSummary.setBackground(rounded(softAccent(accent), 8, accent));

        ImageView icon = new ImageView(this);
        icon.setImageResource(platform == null ? R.drawable.ic_downloads : platform.iconRes);
        if (platform == null) {
            icon.setColorFilter(theme.mutedColor);
        }
        icon.setPadding(dp(4), dp(4), dp(4), dp(4));
        icon.setBackground(rounded(theme.surfaceAltColor, 8, accent));
        lastDownloadSummary.addView(icon, new LinearLayout.LayoutParams(dp(46), dp(46)));

        LinearLayout texts = new LinearLayout(this);
        texts.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        textParams.leftMargin = dp(12);
        lastDownloadSummary.addView(texts, textParams);

        TextView title = textView(file.getName(), 14, accent, true);
        title.setSingleLine(false);
        texts.addView(title);

        String label = (platform == null ? platformName : platform.name) + "  |  " + option.label + "  |  " + readableSize(file.length());
        TextView detail = textView(label, 12, theme.mutedColor, false);
        detail.setPadding(0, dp(4), 0, 0);
        texts.addView(detail);

        lastDownloadSummary.setAlpha(0f);
        lastDownloadSummary.setTranslationY(dp(8));
        lastDownloadSummary.setVisibility(View.VISIBLE);
        lastDownloadSummary.animate().alpha(1f).translationY(0f).setDuration(180L).start();
    }

    private void hideLastDownloadSummary() {
        if (lastDownloadSummary != null) {
            lastDownloadSummary.setVisibility(View.GONE);
            lastDownloadSummary.removeAllViews();
        }
    }

    private static String downloadProgressText(int percent, Long etaInSeconds) {
        String message = "İndiriliyor... %" + percent;
        if (etaInSeconds != null && etaInSeconds > 0L) {
            message += " - kalan " + formatEta(etaInSeconds);
        }
        return message;
    }

    private static String formatEta(long seconds) {
        long safeSeconds = Math.max(0L, seconds);
        long minutes = safeSeconds / 60L;
        long remainingSeconds = safeSeconds % 60L;
        long hours = minutes / 60L;
        long remainingMinutes = minutes % 60L;
        if (hours > 0L) {
            return String.format(Locale.US, "%d sa %02d dk", hours, remainingMinutes);
        }
        if (minutes > 0L) {
            return String.format(Locale.US, "%d dk %02d sn", minutes, remainingSeconds);
        }
        return remainingSeconds + " sn";
    }

    private void startProgressPulse() {
        if (progressBar == null || progressPulseRunning) {
            return;
        }
        progressPulseRunning = true;
        progressBar.setAlpha(1f);
        animateProgressPulse(false);
    }

    private void animateProgressPulse(boolean dim) {
        if (!progressPulseRunning || progressBar == null) {
            return;
        }
        progressBar.animate()
                .alpha(dim ? 0.55f : 1f)
                .setDuration(520L)
                .withEndAction(() -> animateProgressPulse(!dim))
                .start();
    }

    private void stopProgressPulse() {
        progressPulseRunning = false;
        if (progressBar != null) {
            progressBar.animate().cancel();
            progressBar.setAlpha(1f);
        }
    }

    private void setStatus(String message, boolean ok) {
        statusView.setText(ui(message));
        statusView.setTextColor(ok ? activePlatform.accentColor : Color.rgb(173, 52, 52));
    }

    private void addNavItem(LinearLayout row, String label, int iconRes, Runnable action) {
        AppThemeOption theme = currentTheme();
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);
        item.setPadding(dp(8), dp(7), dp(8), dp(7));
        item.setBackground(rounded(theme.surfaceColor, 8, theme.borderColor));
        item.setMinimumWidth(dp(76));
        item.setOnClickListener(v -> action.run());

        ImageView icon = new ImageView(this);
        icon.setImageResource(iconRes);
        if (shouldTintGenericIcon(iconRes)) {
            icon.setColorFilter(theme.mutedColor);
        }
        item.addView(icon, new LinearLayout.LayoutParams(dp(28), dp(28)));

        TextView text = textView(label, 11, Color.rgb(23, 32, 29), false);
        text.setGravity(Gravity.CENTER);
        text.setSingleLine(true);
        text.setEllipsize(TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.topMargin = dp(3);
        item.addView(text, textParams);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.rightMargin = dp(8);
        row.addView(item, params);
    }

    private AppThemeOption currentTheme() {
        String key = getString(PREF_THEME, APP_THEMES[0].key);
        for (AppThemeOption theme : APP_THEMES) {
            if (theme.key.equals(key)) {
                return theme;
            }
        }
        if ("night".equals(key) || "midnight".equals(key)) {
            return APP_THEMES[1];
        }
        return APP_THEMES[0];
    }

    private String[] themeKeys() {
        String[] keys = new String[APP_THEMES.length];
        for (int i = 0; i < APP_THEMES.length; i++) {
            keys[i] = APP_THEMES[i].key;
        }
        return keys;
    }

    private String themeName(String key) {
        for (AppThemeOption theme : APP_THEMES) {
            if (theme.key.equals(key)) {
                return themeName(theme);
            }
        }
        return themeName(APP_THEMES[0]);
    }

    private String themeName(AppThemeOption theme) {
        return isEnglish() ? theme.enName : theme.trName;
    }

    private String themeSubtitle(AppThemeOption theme) {
        return isEnglish() ? theme.enSubtitle : theme.trSubtitle;
    }

    private boolean isEnglish() {
        return "en".equals(selectedLanguage);
    }

    private String ui(String value) {
        if (!isEnglish() || value == null || value.isEmpty()) {
            return value == null ? "" : value;
        }
        if (value.startsWith("Dosyalar: ")) {
            return "Files: " + value.substring("Dosyalar: ".length());
        }
        if (value.startsWith("Seçilen: ")) {
            return "Selected: " + value.substring("Seçilen: ".length());
        }
        if (value.startsWith("Sil (")) {
            return "Delete " + value.substring("Sil ".length());
        }
        if (value.startsWith("Dil: ")) {
            return "Language: " + value.substring("Dil: ".length());
        }
        if (value.startsWith("Aktif dil: ")) {
            return "Active language: " + value.substring("Aktif dil: ".length());
        }
        if (value.startsWith("Playlist durumu")) {
            return value.replace("Playlist durumu", "Playlist status")
                    .replace("Liste alınıyor...", "Loading list...")
                    .replace("Liste alınamadı.", "List could not be loaded.")
                    .replace(" indirildi", " downloaded")
                    .replace(" hata", " error");
        }
        if (value.startsWith("Playlist indiriliyor: ")) {
            return value.replace("Playlist indiriliyor: ", "Playlist downloading: ")
                    .replace(" - video %", " - video %");
        }
        if (value.startsWith("Instagram public medya indiriliyor... %")) {
            return value.replace("Instagram public medya indiriliyor...", "Downloading Instagram public media...");
        }
        if (value.startsWith("İndiriliyor %")) {
            return value.replace("İndiriliyor", "Downloading");
        }
        if (value.startsWith("Kuyruğa eklendi. Bekleyen: ")) {
            return "Added to queue. Waiting: " + value.substring("Kuyruğa eklendi. Bekleyen: ".length());
        }
        if (value.startsWith("Listeden kaldır (")) {
            return value.replace("Listeden kaldır", "Remove from list");
        }
        if (value.endsWith(" kayıt listeden kaldırıldı")) {
            return value.replace(" kayıt listeden kaldırıldı", " record(s) removed from the app list");
        }
        if (value.endsWith(" kayıt uygulama listesinden kaldırılsın mı? Dosyalar klasörden silinmez.")) {
            return value.replace(" kayıt uygulama listesinden kaldırılsın mı? Dosyalar klasörden silinmez.", " record(s) will be removed from the app list. Files will stay in the folder.");
        }
        if (value.endsWith(" dosya silindi")) {
            return value.replace(" dosya silindi", " file(s) deleted");
        }
        if (value.endsWith(" indirme kaydı silinsin mi?")) {
            return value.replace(" indirme kaydı silinsin mi?", " download record(s) will be deleted. Continue?");
        }
        if (value.startsWith("Başlık: ")) {
            return value
                    .replace("Başlık: ", "Title: ")
                    .replace("\nMaksimum kalite: ", "\nMaximum quality: ")
                    .replace("\nİndirme türü seçin:", "\nChoose download type:");
        }
        if (value.startsWith("Seçim: ")) {
            return value.replace("Seçim: ", "Selection: ")
                    .replace("\nDosya: ", "\nFile: ")
                    .replace("\nPlaylist: ", "\nPlaylist: ")
                    .replace(" indirildi", " downloaded")
                    .replace("\nHata: ", "\nErrors: ")
                    .replace("\nPlaylist dosya sayısı: ", "\nPlaylist file count: ")
                    .replace("\nİndirilen dosya sayısı: ", "\nDownloaded file count: ")
                    .replace("\nKlasör: ", "\nFolder: ")
                    .replace("\nUyarı: ", "\nWarning: ");
        }
        if (value.startsWith("Yeni cihaz için ")) {
            return value.replace("Yeni cihaz için ", "For a new device, after ")
                    .replace(" sonrası", "");
        }
        if (value.startsWith("Bu lisans başka cihaza bağlı. Değişim zamanı: ")) {
            return value.replace("Bu lisans başka cihaza bağlı. Değişim zamanı: ", "This license is bound to another device. Change time: ");
        }
        if (value.startsWith("Bu lisans başka bir cihaza bağlı. Cihaz değişimi ")) {
            return value.replace("Bu lisans başka bir cihaza bağlı. Cihaz değişimi ", "This license is bound to another device. Device change is available after ")
                    .replace(" sonrasında yapılabilir.", ".");
        }
        return uiStatic(value);
    }

    private String uiStatic(String value) {
        switch (value) {
            case "Sosyal platformlar tek uygulamada": return "Social platforms in one app";
            case "Link modu": return "Link mode";
            case "Video linki": return "Video link";
            case "Yapıştır": return "Paste";
            case "Temizle": return "Clear";
            case "İndirme seçeneklerini getir": return "Get download options";
            case "Seçenekler hazırlanıyor...": return "Preparing options...";
            case "Hızlı mod: kaliteyi seçin, indirme sırasında en uygun format alınacak.": return "Fast mode: choose quality, the best matching format will be used during download.";
            case "Hızlı seçenekler hazır.": return "Fast options are ready.";
            case "Tüm playlist'i indir": return "Download full playlist";
            case "Listedeki tüm videoları seçilen formatla indir": return "Download every video in the list with the selected format";
            case "Playlist modu: listedeki tüm videolar seçilen formatla indirilecek.\nİndirme türü seçin:": return "Playlist mode: every video in the list will be downloaded with the selected format.\nChoose download type:";
            case "Playlist sayfası hazır.": return "Playlist page is ready.";
            case "Playlist seçenekleri hazır.": return "Playlist options are ready.";
            case "Playlist listesi alınıyor...": return "Loading playlist list...";
            case "İndirme başlatılıyor...": return "Starting download...";
            case "Kuyruktaki indirme başlatılıyor...": return "Starting queued download...";
            case "Instagram public medya aranıyor...": return "Looking for Instagram public media...";
            case "Instagram public medya ses olarak hazırlanıyor...": return "Preparing Instagram public media as audio...";
            case "Instagram public medya indiriliyor...": return "Downloading Instagram public media...";
            case "Instagram public medya indirildi.": return "Instagram public media downloaded.";
            case "Playlist tamamlandı.": return "Playlist complete.";
            case "Playlist listesi alınamadı. Bağlantıyı kontrol edip tekrar deneyin.": return "Could not load the playlist. Check the connection and try again.";
            case "Playlist indirilemedi. Hiç dosya tamamlanmadı.": return "Playlist could not be downloaded. No file was completed.";
            case "Playlist durumu": return "Playlist status";
            case "Liste alınıyor...": return "Loading list...";
            case "Liste alınamadı.": return "List could not be loaded.";
            case "Bekliyor": return "Waiting";
            case "İndiriliyor": return "Downloading";
            case "Tamamlandı": return "Done";
            case "Atlandı": return "Skipped";
            case "Atla": return "Skip";
            case "Oynat": return "Play";
            case "Hata": return "Error";
            case "İndirme iptal edildi.": return "Download cancelled.";
            case "Sıradaki video başlatılıyor...": return "Starting next video...";
            case "Playlist kısmen tamamlandı.": return "Playlist partially completed.";
            case "Oturumla tekrar dene": return "Try with session";
            case "Girişsiz indirme başarısız oldu. Bu içerik gizli, kısıtlı veya oturum çerezi istiyor olabilir. İsterseniz uygulama içindeki platform ekranında giriş yapıp videoyu oradan indirebilirsiniz.": return "Download without sign-in failed. This content may be private, restricted, or require session cookies. You can sign in from the platform screen inside the app and download the video there.";
            case "Instagram girişsiz yanıt vermedi. Gönderi gizli, kısıtlı veya oturum çerezi istiyor olabilir. Uygulama içindeki Instagram ekranında giriş yapıp videoyu açarak tekrar deneyebilirsiniz.": return "Instagram did not respond without sign-in. The post may be private, restricted, or require session cookies. You can sign in from the Instagram screen inside the app, open the video, and try again.";
            case "Instagram public video yanıtı alınamadı. Video herkese açık olsa bile Instagram bazen boş medya yanıtı döndürebiliyor. İndirme motorunu güncelleyip tekrar deneyin; devam ederse linki Instagram ekranında açıp Videoyu indir butonunu kullanın.": return "Instagram did not return a public video response. Even when the video is public, Instagram can sometimes return an empty media response. Update the download engine and try again; if it continues, open the link in the Instagram screen and use the Download video button.";
            case "Facebook girişsiz yanıt vermedi. Video gizli, kısıtlı veya oturum çerezi istiyor olabilir. Uygulama içindeki Facebook ekranında giriş yapıp videoyu açarak tekrar deneyebilirsiniz.": return "Facebook did not respond without sign-in. The video may be private, restricted, or require session cookies. You can sign in from the Facebook screen inside the app, open the video, and try again.";
            case "Girişsiz indirme başarısız oldu. İçerik gizli, kısıtlı veya oturum çerezi istiyor olabilir. Uygulama içindeki platform ekranında giriş yapıp tekrar deneyebilirsiniz.": return "Download without sign-in failed. The content may be private, restricted, or require session cookies. You can sign in from the platform screen inside the app and try again.";
            case "YouTube sunucusuna ulaşılamadı. İnternet/DNS bağlantısını kontrol edin. VPN, özel DNS veya reklam engelleyici kullanıyorsanız kapatıp tekrar deneyin.": return "Could not reach the YouTube server. Check your internet/DNS connection. If you use VPN, private DNS, or an ad blocker, turn it off and try again.";
            case "Ağ bağlantısı zaman aşımına uğradı. Bağlantıyı kontrol edip tekrar deneyin.": return "The network connection timed out. Check the connection and try again.";
            case "Kalite seçenekleri henüz alınmadı.": return "Quality options have not been loaded yet.";
            case "İndir": return "Download";
            case "İptal": return "Cancel";
            case "Başlatılıyor...": return "Starting...";
            case "Aç": return "Open";
            case "Paylaş": return "Share";
            case "İndirme motorunu güncelle": return "Update download engine";
            case "Oturumlar WebView içinde kalır. Yalnızca size ait veya indirme yetkiniz olan içerikleri indirin.": return "Sessions stay inside WebView. Download only content you own or are authorized to save.";
            case "Platformlar": return "Platforms";
            case "Platform seç": return "Choose platform";
            case "paneli": return "panel";
            case "Akışı aç": return "Open feed";
            case "Menü": return "Menu";
            case "Ana": return "Home";
            case "Ana ekran": return "Home";
            case "Tanıtım ve hızlı kısayollar": return "Overview and quick shortcuts";
            case "Link ile indir": return "Download by link";
            case "Paylaşılan bağlantıdan seçenekleri getir": return "Get options from a shared link";
            case "Aktif platform akışı": return "Active platform feed";
            case "Seçili platformda gezin": return "Browse the selected platform";
            case "İndirilenler": return "Downloads";
            case "Dosyaları aç veya paylaş": return "Open or share files";
            case "Ayarlar": return "Settings";
            case "Kalite, dil, görünüm ve bildirimler": return "Quality, language, appearance, and notifications";
            case "Hakkında": return "About";
            case "Ahmet Doğan ve MetaFold bilgileri": return "Ahmet Doğan and MetaFold info";
            case "Geliştiriciye kahve ısmarla": return "Buy the developer a coffee";
            case "IBAN kopyala": return "Copy IBAN";
            case "IBAN kopyalandı": return "IBAN copied";
            case "IBAN bilgisi yok": return "No IBAN info";
            case "Destek sayfasını aç": return "Open support page";
            case "IBAN bilgisi henüz eklenmedi. Destek hesabını bağlamak için geliştirici IBAN bilgisini eklemeli.": return "IBAN information has not been added yet. Add the developer IBAN to enable bank transfer support.";
            case "Temalar": return "Themes";
            case "Video bağlantılarını kalite seçerek indirin, platform akışlarına uygulama içinden geçin ve lisans/güncellemeleri tek yerden yönetin.": return "Download video links with quality choices, browse platform feeds inside the app, and manage licenses and updates in one place.";
            case "Platform kısayolları": return "Platform shortcuts";
            case "Hızlı başlangıç": return "Quick start";
            case "Bir platform akışını açabilir, başka uygulamadan paylaşılan linki yakalayabilir veya linki buraya yapıştırıp MP3/720p/1080p gibi seçeneklerle indirebilirsiniz.": return "Open a platform feed, catch links shared from another app, or paste a link here and download with options such as MP3, 720p, or 1080p.";
            case "Tema seç": return "Choose theme";
            case "Tema değiştirildi": return "Theme changed";
            case "Geri": return "Back";
            case "Videoyu indir": return "Download video";
            case "İndirilenlerde ara": return "Search downloads";
            case "Listeyi yenile": return "Refresh list";
            case "Listeyi temizle": return "Clear list";
            case "Tümünü seç": return "Select all";
            case "Seçimi kaldır": return "Clear selection";
            case "Sil": return "Delete";
            case "Listeden kaldır": return "Remove from list";
            case "Bitti": return "Done";
            case "Henüz indirilen dosya yok.": return "No downloads yet.";
            case "İndirme listesi temiz. Dosyalar klasörde duruyor.": return "Download list is clear. Files are still in the folder.";
            case "Aramaya uygun dosya bulunamadı.": return "No files match your search.";
            case "Seçilecek dosya yok": return "No files to select";
            case "Önce dosya seçin": return "Select a file first";
            case "Seçilenleri sil": return "Delete selected";
            case "Seçilenleri listeden kaldır": return "Remove selected from list";
            case "İndirme listesini temizle": return "Clear download list";
            case "Temizlenecek kayıt yok": return "No records to clear";
            case "Evet": return "Yes";
            case "Vazgeç": return "Cancel";
            case "Video ve ses": return "Video and audio";
            case "Varsayılan kalite, format, ses tercihleri": return "Default quality, format, and audio preferences";
            case "Varsayılan çözünürlük": return "Default resolution";
            case "Yüksek çözünürlükleri göster": return "Show high resolutions";
            case "2K/4K seçeneklerini kalite listesinde göster": return "Show 2K/4K options in the quality list";
            case "Varsayılan video biçimi": return "Default video format";
            case "Varsayılan ses biçimi": return "Default audio format";
            case "Kalite sıralaması": return "Quality order";
            case "Seçenekler videonun en yüksek kalitesinden aşağı sıralanır": return "Options are sorted from the video's highest quality downward";
            case "Video indirme klasörü": return "Video download folder";
            case "Klasör, dosya adı, deneme ve kuyruk ayarları": return "Folder, filename, retry, and queue settings";
            case "Ses indirme klasörü": return "Audio download folder";
            case "Dosya adlarında izin verilen karakterler": return "Allowed filename characters";
            case "Değiştirme karakteri": return "Replacement character";
            case "Azami deneme sayısı": return "Maximum retry count";
            case "İndirme kuyruğu": return "Download queue";
            case "İndirme sürerken yeni seçimleri sıraya ekle": return "Queue new selections while a download is running";
            case "Görünüm": return "Appearance";
            case "Tema": return "Theme";
            case "Arayüzün sakin renk paleti": return "Calm interface color palette";
            case "Yüksek yenileme hızı": return "High refresh rate";
            case "Ekran destekliyorsa 90/120 Hz modunu kullan": return "Use 90/120 Hz when the screen supports it";
            case "Platform renkleri": return "Platform colors";
            case "Platform logoları ve indirilen kartları kendi marka rengiyle görünür": return "Platform logos and download cards keep their brand color";
            case "Menü animasyonu": return "Menu animation";
            case "Sol menü kayarak açılır ve kapanır": return "The drawer slides open and closed";
            case "Veri ve önbellek": return "Data and cache";
            case "Geçici dosyaları ve çerezleri temizle": return "Clear temporary files and cookies";
            case "Önbelleğe alınmış meta verileri sil": return "Delete cached metadata";
            case "Tüm geçici web ve video verilerini kaldır": return "Remove temporary web and video data";
            case "Önbelleği temizle": return "Clear cache";
            case "Geçici video ve web verileri silinsin mi?": return "Delete temporary video and web data?";
            case "Önbellek temizlendi": return "Cache cleared";
            case "Web oturum çerezlerini temizle": return "Clear web session cookies";
            case "Uygulama içi platform girişlerini sıfırla": return "Reset in-app platform logins";
            case "Çerezleri temizle": return "Clear cookies";
            case "Platform girişleri ve oturum çerezleri silinsin mi?": return "Delete platform logins and session cookies?";
            case "Çerezler temizlendi": return "Cookies cleared";
            case "İçerik": return "Content";
            case "Dil ve platform içerik davranışı": return "Language and platform content behavior";
            case "Uygulama dili": return "App language";
            case "Dil değiştirildi": return "Language changed";
            case "Oturumlu içerik": return "Logged-in content";
            case "Seçili platformda giriş sayfasını aç": return "Open the login page for the selected platform";
            case "Platform akışı": return "Platform feed";
            case "Seçili platformu uygulama içinde aç": return "Open the selected platform inside the app";
            case "Güncellemeler": return "Updates";
            case "Yeni sürüm bildirimi ve elle denetleme": return "New version notifications and manual checks";
            case "Yeni sürüm olduğunda uygulama güncellemesi için bildirim göster": return "Show a notification when a new app version is available";
            case "Güncellemeleri denetle": return "Check for updates";
            case "Yeni sürümleri el ile denetleyin": return "Manually check for new versions";
            case "Güncellemeler denetleniyor": return "Checking updates";
            case "Uygulama sürümü": return "App version";
            case "Uygulama güncellemesini denetle": return "Check app update";
            case "GitHub Releases üzerinden yeni APK sürümünü kontrol et": return "Check the new APK version through GitHub Releases";
            case "GitHub release sayfasını aç": return "Open GitHub release page";
            case "Yeni APK dosyaları bu repodaki release bölümünden alınır": return "New APK files are fetched from this repository's release section";
            case "yt-dlp çekirdeğini son kararlı sürüme yükselt": return "Upgrade the yt-dlp core to the latest stable version";
            case "Güncelleme kaynağı": return "Update source";
            case "Uygulama güncellemesi denetleniyor...": return "Checking app update...";
            case "Uygulama güncellemesi denetlenemedi.": return "App update could not be checked.";
            case "Uygulama güncel.": return "App is up to date.";
            case "Uygulama güncel": return "App is up to date";
            case "Kurulu sürüm": return "Installed version";
            case "GitHub sürümü": return "GitHub version";
            case "Yeni sürüm hazır": return "New version is ready";
            case "Yeni sürüm": return "New version";
            case "Güncelleme GitHub Releases üzerinden alınacak.": return "The update will be fetched through GitHub Releases.";
            case "Güncelleme uygulama içinde indirilecek. Kurulum ekranında Android onayı gereklidir.": return "The update will be downloaded inside the app. Android approval is required on the installer screen.";
            case "İndir ve kur": return "Download and install";
            case "Güncelleme indiriliyor": return "Downloading update";
            case "Güncelleme indiriliyor...": return "Downloading update...";
            case "APK hazırlanıyor": return "Preparing APK";
            case "Bağlantı kuruluyor...": return "Connecting...";
            case "Dosya boyutu hesaplanıyor": return "Calculating file size";
            case "Güncelleme hazırlandı": return "Update is ready";
            case "İndirme tamamlanınca kurulum ekranı açılacak.": return "The installer will open when the download finishes.";
            case "Güncelleme indirildi. Kurulum ekranı açılıyor...": return "Update downloaded. Opening installer...";
            case "Güncelleme indirilemedi.": return "Update could not be downloaded.";
            case "Güncelleme indirilemedi": return "Update could not be downloaded";
            case "Güncelleme APK dosyası bulunamadı.": return "Update APK file was not found.";
            case "Kurulum izni gerekli": return "Install permission required";
            case "Android bu uygulamanın APK yükleyebilmesi için izin istiyor. İzni verdikten sonra kurulum ekranı otomatik açılacak.": return "Android requires permission before this app can install APKs. After you allow it, the installer will open automatically.";
            case "Ayarları aç": return "Open settings";
            case "Kurulum izni ekranı açılamadı.": return "Install permission settings could not be opened.";
            case "Kurulum ekranı açıldı. Devam etmek için Android onayını verin.": return "Installer opened. Continue by approving the Android prompt.";
            case "Kurulum ekranı açılamadı.": return "Installer could not be opened.";
            case "Kurulum ekranı açılamadı": return "Installer could not be opened";
            case "Zorunlu güncelleme": return "Required update";
            case "Bu sürüm artık kullanılamaz.": return "This version can no longer be used.";
            case "Devam etmek için uygulamayı güncelleyin.": return "Update the app to continue.";
            case "Uygulamayı kapat": return "Close app";
            case "Release sayfasını aç": return "Open release page";
            case "APK'yı aç": return "Open APK";
            case "Güncelleme bulundu": return "Update found";
            case "Güncelle": return "Update";
            case "Daha sonra hatırlat": return "Remind me later";
            case "Daha sonra hatırlatılacak": return "You will be reminded later";
            case "Tamam": return "OK";
            case "Kayıt ol": return "Register";
            case "Giriş yap": return "Sign in";
            case "Güvenli indirme hesabınız": return "Your secure download account";
            case "Hesabınıza giriş yapın": return "Sign in to your account";
            case "Hesabınızı oluşturun": return "Create your account";
            case "MetaFold Downloader kullanmak için e-posta hesabınızla devam edin.": return "Continue with your email account to use MetaFold Downloader.";
            case "E-posta": return "Email";
            case "Şifre": return "Password";
            case "Şifre tekrar": return "Repeat password";
            case "Hesabınız yok mu? Kayıt ol": return "No account? Register";
            case "Zaten hesabınız var mı? Giriş yap": return "Already have an account? Sign in";
            case "Şifremi unuttum": return "Forgot password";
            case "Kayıt isteği oluştuktan sonra kullanım için yönetici onayı gerekir.": return "After registration, admin approval is required before use.";
            case "Şifre en az 6 karakter olmalı": return "Password must be at least 6 characters";
            case "Şifreler eşleşmiyor": return "Passwords do not match";
            case "Kayıt oluşturuluyor...": return "Creating account...";
            case "Giriş yapılıyor...": return "Signing in...";
            case "Hesabınız onaylı. Uygulamayı kullanabilirsiniz.": return "Your account is approved. You can use the app.";
            case "Kayıt isteğiniz alındı. Yönetici onayından sonra indirme özellikleri açılır.": return "Your registration request was received. Download features unlock after admin approval.";
            case "Kayıt başarısız": return "Registration failed";
            case "Giriş başarısız": return "Sign in failed";
            case "Şifre sıfırlama e-postası gönderiliyor...": return "Sending password reset email...";
            case "Şifre sıfırlama": return "Password reset";
            case "Şifre sıfırlama bağlantısı e-posta adresinize gönderildi.": return "A password reset link was sent to your email address.";
            case "Şifre sıfırlama başarısız": return "Password reset failed";
            case "Giriş gerekli": return "Sign in required";
            case "Devam etmek için kayıt olun veya giriş yapın.": return "Register or sign in to continue.";
            case "Çıkış yapıldı": return "Signed out";
            case "Bu e-posta zaten kayıtlı. Giriş yapın.": return "This email is already registered. Sign in.";
            case "Bu e-posta ile hesap bulunamadı.": return "No account was found for this email.";
            case "E-posta veya şifre hatalı.": return "Email or password is incorrect.";
            case "Şifre en az 6 karakter olmalı.": return "Password must be at least 6 characters.";
            case "Bu kullanıcı hesabı devre dışı.": return "This user account is disabled.";
            case "Çok fazla deneme yapıldı. Biraz sonra tekrar deneyin.": return "Too many attempts. Try again later.";
            case "Firebase Authentication içinde Email/Password girişi etkinleştirilmeli.": return "Email/Password sign-in must be enabled in Firebase Authentication.";
            case "Firebase Authentication kurulumu bulunamadı. Firebase Console'da Authentication > Sign-in method bölümünden Email/Password girişini etkinleştirin.": return "Firebase Authentication setup was not found. Enable Email/Password sign-in from Firebase Console > Authentication > Sign-in method.";
            case "Firestore izinleri kayıt isteğini reddetti. Firebase Console > Firestore > Rules ekranında uygulamanın güncel firestore.rules içeriğini Publish edin.": return "Firestore permissions rejected the registration request. Publish the app's latest firestore.rules content from Firebase Console > Firestore > Rules.";
            case "Firebase Web API Key geçersiz veya bu proje için değil.": return "The Firebase Web API Key is invalid or belongs to another project.";
            case "Firebase oturum hatası.": return "Firebase sign-in error.";
            case "Oturum süresi doldu. Lütfen çıkış yapıp tekrar giriş yapın.": return "Session expired. Please sign out and sign in again.";
            case "Oturum": return "Session";
            case "Giriş yapılmadı": return "Not signed in";
            case "Lisans kontrol ediliyor...": return "Checking license...";
            case "Kayıt / giriş ekranı": return "Register / sign in screen";
            case "E-posta ve şifre ile oturum aç": return "Sign in with email and password";
            case "Onay isteğini yenile": return "Refresh approval request";
            case "Çıkış yap": return "Sign out";
            case "Bu cihazdaki oturumu ve lisans bilgisini temizle": return "Clear the session and license info on this device";
            case "Bu cihazdaki oturum kapatılsın mı?": return "Sign out on this device?";
            case "Lisanslı cihaz": return "Licensed device";
            case "Cihaz değişim kilidi": return "Device change lock";
            case "Henüz cihaz bağlanmadı": return "No device bound yet";
            case "Bu cihaz": return "This device";
            case "Başka cihaz": return "Another device";
            case "Kayıt sonrası 7 gün kilitlenir": return "Locked for 7 days after registration";
            case "Cihaz değişimi yapılabilir": return "Device change is available";
            case "Bu lisans başka cihaza bağlı": return "This license is bound to another device";
            case "Bu lisans başka bir cihaza bağlı. Cihaz değişimi ": return "This license is bound to another device. Device change is available after ";
            case "Cihaz değişimi yapıldı. Bu hesap 7 gün boyunca bu cihaza bağlı kalacak.": return "Device change completed. This account will stay bound to this device for 7 days.";
            case "Cihaz değişimi şu anda yapılamıyor. Yeniden denemeden önce lisans ekranından onay durumunu kontrol edin.": return "Device change is not available right now. Check approval status on the license screen before trying again.";
            case "Lisans": return "License";
            case "Lisans satın al": return "Buy license";
            case "Lisans satın al (WhatsApp)": return "Buy license (WhatsApp)";
            case "Ahmet Doğan'a lisans isteği gönder": return "Send a license request to Ahmet Dogan";
            case "WhatsApp açılamadı": return "WhatsApp could not be opened";
            case "Lisans anahtarı ve cihaz doğrulaması": return "License key and device verification";
            case "Lisans durumu": return "License status";
            case "Kayıt e-postası": return "Registration email";
            case "Cihaz kimliği": return "Device ID";
            case "E-posta ile kayıt ol": return "Register with email";
            case "Kullanım isteği oluştur ve yönetici onayı bekle": return "Create a usage request and wait for admin approval";
            case "Lisans anahtarı gir": return "Enter license key";
            case "Onay durumunu kontrol et": return "Check approval status";
            case "Sunucudan lisans/onay durumunu denetle": return "Check license/approval status from the server";
            case "Lisansı kaldır": return "Remove license";
            case "Bu cihazdaki kayıtlı lisans anahtarını temizle": return "Clear the saved license key on this device";
            case "Kayıtlı lisans bilgileri bu cihazdan silinsin mi?": return "Delete the saved license information from this device?";
            case "Doğrulama modeli": return "Verification model";
            case "E-posta/lisans anahtarı + cihaz kimliği sunucuda onaylanır; onay yoksa indirme kullanılamaz": return "Email/license key and device ID are approved on the server; downloads are unavailable without approval";
            case "Lisans anahtarı": return "License key";
            case "Satın alınan lisans anahtarını girin. Anahtar bu cihaza bağlanarak doğrulanır.": return "Enter the purchased license key. The key is verified by binding it to this device.";
            case "Kaydet ve doğrula": return "Save and verify";
            case "Lisans anahtarı boş olamaz": return "License key cannot be empty";
            case "E-posta adresi": return "Email address";
            case "E-posta ile kayıt": return "Register with email";
            case "E-posta adresiniz ve cihaz kimliğiniz yönetici onayına gönderilecek. Onay verilince uygulama kullanılabilir.": return "Your email address and device ID will be sent for admin approval. The app can be used after approval.";
            case "Kayıt isteği gönder": return "Send registration request";
            case "Geçerli bir e-posta girin": return "Enter a valid email";
            case "Onay bekleniyor": return "Waiting for approval";
            case "Kayıt isteği cihazda hazırlandı. Gerçek onay takibi için LICENSE_REGISTER_URL değerine kayıt API adresi eklenmeli.": return "The registration request was prepared on this device. Add the registration API URL to LICENSE_REGISTER_URL for real approval tracking.";
            case "Kayıt isteği gönderiliyor...": return "Sending registration request...";
            case "Kayıt isteği gönderilemedi": return "Registration request could not be sent";
            case "Önce e-posta ile kayıt olun": return "Register with email first";
            case "Lisans sunucusu bekleniyor": return "Waiting for license server";
            case "Lisans bilgisi kaydedildi. Gerçek doğrulama için LICENSE_VALIDATE_URL değerine lisans API adresi eklenmeli.": return "The license info was saved. Add the license API URL to LICENSE_VALIDATE_URL for real verification.";
            case "Lisans doğrulanıyor...": return "Verifying license...";
            case "Lisans etkin": return "License active";
            case "Lisans doğrulanamadı": return "License could not be verified";
            case "Lisans pasif": return "License inactive";
            case "Firebase ayarı eksik": return "Firebase setting missing";
            case "Spark plan lisans sistemi için Firebase Web API Key uygulamaya eklenmeli.": return "Add the Firebase Web API Key to the app for the Spark plan license system.";
            case "Etkin": return "Active";
            case "Anahtar kaydedildi, sunucu doğrulaması bekleniyor": return "Key saved, waiting for server verification";
            case "Pasif veya doğrulanamadı": return "Inactive or not verified";
            case "Lisans girilmedi": return "No license entered";
            case "Henüz lisans anahtarı girilmedi": return "No license key entered yet";
            case "Henüz e-posta ile kayıt olunmadı": return "No email registration yet";
            case "Onay gerekli": return "Approval required";
            case "Bu özelliği kullanmak için e-posta ile kayıt olunmalı ve yönetici onayı verilmelidir.": return "To use this feature, register with email and wait for admin approval.";
            case "Lisans ekranı": return "License screen";
            case "İndirme motoru sürümü": return "Download engine version";
            case "Tam ekranı tekrar uygula": return "Reapply fullscreen";
            case "Bildirim ve gezinme çubuklarını yeniden gizle": return "Hide notification and navigation bars again";
            case "Tam ekran yenilendi": return "Fullscreen refreshed";
            case "Video linki gerekli.": return "Video link is required.";
            case "Desteklenen platformlar: YouTube, Instagram, Facebook, TikTok, Pinterest, X.": return "Supported platforms: YouTube, Instagram, Facebook, TikTok, Pinterest, X.";
            case "Video bilgisi alınamadı.": return "Could not get video info.";
            case "Kalite seçenekleri alınamadı.": return "Quality options could not be loaded.";
            case "Seçenekler alınıyor...": return "Loading options...";
            case "Oturumlu sayfa hazır.": return "Logged-in page is ready.";
            case "Seçenekler hazır.": return "Options are ready.";
            case "Önce indirme seçeneklerini getirip kalite seçin.": return "Get download options and choose quality first.";
            case "MP3 için FFmpeg hazır değil.": return "FFmpeg is not ready for MP3.";
            case "Hazırlanıyor...": return "Preparing...";
            case "Kuyruktaki indirme hazırlanıyor...": return "Preparing queued download...";
            case "İndirme tamamlandı.": return "Download complete.";
            case "İndirme başarısız.": return "Download failed.";
            case "İptal ediliyor...": return "Cancelling...";
            case "Güncelleme başarısız.": return "Update failed.";
            case "İndirme motoru güncelleniyor...": return "Updating download engine...";
            case "İndirme motoru güncellendi": return "Download engine updated";
            case "Hazir":
            case "Hazır": return "Ready";
            case "Önce video sayfasını açın": return "Open a video page first";
            case "İndirme zaten çalışıyor": return "A download is already running";
            case "İndirme sırasında güncelleme yapılamaz": return "Cannot update while downloading";
            case "Önce bir video indirin": return "Download a video first";
            case "Dosya bulunamadı": return "File not found";
            case "Panoda link yok": return "No link in clipboard";
            case "Uygun uygulama bulunamadı.": return "No suitable app found.";
            case "Site açılamadı": return "Site could not be opened";
            case "Klasör seçici açılamadı": return "Folder picker could not be opened";
            case "İndirme klasörü seçildi": return "Download folder selected";
            case "Çıkmak için tekrar geri tuşuna basın": return "Press Back again to exit";
            case "Desteklenmeyen link": return "Unsupported link";
            case "En iyi": return "Best";
            case "Çoğu özel karakterler": return "Most special characters";
            case "Kısıtlı karakterler": return "Restricted characters";
            case "ASCII güvenli": return "ASCII safe";
            default: return value;
        }
    }

    private TextView textView(String value, int sp, int color, boolean bold) {
        TextView textView = new TextView(this);
        textView.setText(ui(value));
        textView.setTextSize(sp);
        textView.setTextColor(themedTextColor(color));
        textView.setIncludeFontPadding(true);
        if (bold) {
            textView.setTypeface(Typeface.DEFAULT_BOLD);
        }
        return textView;
    }

    private Button primaryButton(String text, int color) {
        Button button = new Button(this);
        button.setAllCaps(false);
        button.setText(ui(text));
        button.setTextSize(15);
        button.setTextColor(readableOn(color));
        button.setBackground(rounded(color, 8, color));
        button.setMinHeight(dp(48));
        return button;
    }

    private Button secondaryButton(String text) {
        Button button = new Button(this);
        button.setAllCaps(false);
        button.setText(ui(text));
        button.setTextSize(14);
        AppThemeOption theme = currentTheme();
        button.setTextColor(theme.textColor);
        button.setBackground(rounded(theme.surfaceAltColor, 8, theme.borderColor));
        button.setMinHeight(dp(44));
        return button;
    }

    private void setButtonColor(Button button, int color) {
        button.setBackground(rounded(color, 8, color));
        button.setTextColor(readableOn(color));
    }

    private int themedTextColor(int color) {
        AppThemeOption theme = currentTheme();
        if (color == Color.rgb(23, 32, 29) || color == Color.BLACK) {
            return theme.textColor;
        }
        if (color == Color.rgb(96, 112, 106)) {
            return theme.mutedColor;
        }
        return color;
    }

    private int softAccent(int color) {
        AppThemeOption theme = currentTheme();
        if (isDarkTheme(theme)) {
            return blend(theme.surfaceColor, color, 0.22f);
        }
        return lighten(color);
    }

    private int readableOn(int color) {
        return luminance(color) > 0.62 ? Color.rgb(18, 24, 25) : Color.WHITE;
    }

    private boolean isDarkTheme(AppThemeOption theme) {
        return luminance(theme.backgroundColor) < 0.35;
    }

    private int blend(int base, int overlay, float overlayWeight) {
        float clamped = Math.max(0f, Math.min(1f, overlayWeight));
        float baseWeight = 1f - clamped;
        return Color.rgb(
                Math.round(Color.red(base) * baseWeight + Color.red(overlay) * clamped),
                Math.round(Color.green(base) * baseWeight + Color.green(overlay) * clamped),
                Math.round(Color.blue(base) * baseWeight + Color.blue(overlay) * clamped)
        );
    }

    private static double luminance(int color) {
        return (0.2126 * Color.red(color) + 0.7152 * Color.green(color) + 0.0722 * Color.blue(color)) / 255.0;
    }

    private int topSafeInsetFallback() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return dp(24);
    }

    private boolean shouldTintGenericIcon(int iconRes) {
        return iconRes == R.drawable.ic_downloads
                || iconRes == R.drawable.ic_settings
                || iconRes == R.drawable.ic_about
                || iconRes == R.drawable.ic_link
                || iconRes == R.drawable.ic_home;
    }

    private static int lighten(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.rgb(
                red + Math.round((255 - red) * 0.88f),
                green + Math.round((255 - green) * 0.88f),
                blue + Math.round((255 - blue) * 0.88f)
        );
    }

    private GradientDrawable rounded(int fill, int radiusDp, int stroke) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(fill);
        drawable.setCornerRadius(dp(radiusDp));
        drawable.setStroke(dp(1), stroke);
        return drawable;
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
    }

    private LinearLayout.LayoutParams rowWeight() {
        return new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private void toast(String text) {
        Toast.makeText(this, ui(text), Toast.LENGTH_SHORT).show();
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024L) {
            return bytes + " B";
        }
        double kb = bytes / 1024.0;
        if (kb < 1024.0) {
            return String.format(Locale.US, "%.1f KB", kb);
        }
        double mb = kb / 1024.0;
        if (mb < 1024.0) {
            return String.format(Locale.US, "%.1f MB", mb);
        }
        return String.format(Locale.US, "%.1f GB", mb / 1024.0);
    }

    private static boolean hasVideo(VideoFormat format) {
        String vcodec = format.getVcodec();
        return vcodec == null || !"none".equalsIgnoreCase(vcodec);
    }

    private static int maxHeight(List<DownloadOption> options) {
        int max = 0;
        for (DownloadOption option : options) {
            if (!option.audio && option.height > max) {
                max = option.height;
            }
        }
        return max;
    }

    private static String labelForHeight(int height) {
        if (height >= 4320) {
            return "8K (" + height + "p)";
        }
        if (height >= 2160) {
            return "4K (" + height + "p)";
        }
        if (height >= 1440) {
            return "2K (" + height + "p)";
        }
        return height + "p";
    }

    private static int resolutionHeight(String value) {
        if ("4K".equals(value)) {
            return 2160;
        }
        if ("2K".equals(value)) {
            return 1440;
        }
        if (value != null && value.endsWith("p")) {
            try {
                return Integer.parseInt(value.substring(0, value.length() - 1));
            } catch (NumberFormatException ignored) {
                return 0;
            }
        }
        return 0;
    }

    private static String videoFormatFilter(int height, String container) {
        String heightFilter = "[height<=" + height + "]";
        if ("webm".equals(container)) {
            return "bestvideo" + heightFilter + "[ext=webm]+bestaudio[ext=webm]/best" + heightFilter + "[ext=webm]/best" + heightFilter + "/best";
        }
        return "bestvideo" + heightFilter + "[ext=mp4]+bestaudio[ext=m4a]/best" + heightFilter + "[ext=mp4]/best" + heightFilter + "/best";
    }

    private static String languageName(String language) {
        return "en".equals(language) ? "English" : "Türkçe";
    }

    private static String readableSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        double kb = bytes / 1024.0;
        if (kb < 1024) {
            return String.format(Locale.US, "%.1f KB", kb);
        }
        double mb = kb / 1024.0;
        if (mb < 1024) {
            return String.format(Locale.US, "%.1f MB", mb);
        }
        return String.format(Locale.US, "%.1f GB", mb / 1024.0);
    }

    private static String firstNonEmpty(String... values) {
        for (String value : values) {
            if (!TextUtils.isEmpty(value)) {
                return value;
            }
        }
        return "";
    }

    private static String trimForUi(String value, int max) {
        if (value == null || value.length() <= max) {
            return value == null ? "" : value;
        }
        return value.substring(0, max) + "...";
    }

    private static void addUnique(List<String> list, String value) {
        if (!TextUtils.isEmpty(value) && !list.contains(value)) {
            list.add(value);
        }
    }

    private static String hostOf(String url) {
        try {
            return new URI(url).getHost();
        } catch (Exception error) {
            return "";
        }
    }

    private static String cookieDomain(String host) {
        String normalized = host.toLowerCase(Locale.US);
        if (normalized.endsWith("youtube.com") || normalized.equals("youtu.be")) {
            return ".youtube.com";
        }
        if (normalized.endsWith("google.com")) {
            return ".google.com";
        }
        if (normalized.endsWith("instagram.com")) {
            return ".instagram.com";
        }
        if (normalized.endsWith("facebook.com") || normalized.equals("fb.watch")) {
            return ".facebook.com";
        }
        if (normalized.endsWith("tiktok.com")) {
            return ".tiktok.com";
        }
        if (normalized.endsWith("pinterest.com") || normalized.equals("pin.it")) {
            return normalized.equals("pin.it") ? ".pin.it" : ".pinterest.com";
        }
        if (normalized.endsWith("x.com")) {
            return ".x.com";
        }
        if (normalized.endsWith("twitter.com")) {
            return ".twitter.com";
        }
        return normalized.startsWith(".") ? normalized : "." + normalized;
    }

    private static SocialPlatform platformByName(String name) {
        for (SocialPlatform platform : PLATFORMS) {
            if (platform.name.equals(name)) {
                return platform;
            }
        }
        return null;
    }

    private static SocialPlatform platformByDetectedName(String detectedName) {
        if (TextUtils.isEmpty(detectedName)) {
            return null;
        }
        for (SocialPlatform platform : PLATFORMS) {
            if (platform.name.equals(detectedName)) {
                return platform;
            }
        }
        return null;
    }

    private static void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024 * 64];
        int read;
        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }
    }

    private static String compactLine(String line, int max) {
        String trimmed = line == null ? "" : line.trim();
        if (trimmed.length() <= max) {
            return trimmed;
        }
        return trimmed.substring(0, max) + "...";
    }

    private static String cleanError(Throwable error) {
        String message = rawErrorText(error);
        String cleaned = stripOutdatedWarning(message == null ? "Bilinmeyen hata" : message);
        String lower = cleaned.toLowerCase(Locale.US);
        if (isInstagramPublicExtractorError("Instagram", cleaned)) {
            return instagramPublicExtractorMessage();
        }
        if (isPlatformLoginError("Instagram", cleaned)) {
            return platformLoginMessage("Instagram");
        }
        if (isPlatformLoginError("Facebook", cleaned)) {
            return platformLoginMessage("Facebook");
        }
        if (lower.contains("no address associated with hostname")
                || lower.contains("unable to download api page")
                || lower.contains("transporterror")
                || lower.contains("failed to resolve")) {
            return "YouTube sunucusuna ulaşılamadı. İnternet/DNS bağlantısını kontrol edin. VPN, özel DNS veya reklam engelleyici kullanıyorsanız kapatıp tekrar deneyin.";
        }
        if (lower.contains("timed out") || lower.contains("connection reset") || lower.contains("network is unreachable")) {
            return "Ağ bağlantısı zaman aşımına uğradı. Bağlantıyı kontrol edip tekrar deneyin.";
        }
        return compactLine(cleaned, 1200);
    }

    private static String rawErrorText(Throwable error) {
        if (error == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(error.getMessage())) {
            builder.append(error.getMessage());
        }
        Throwable cause = error.getCause();
        while (cause != null) {
            if (!TextUtils.isEmpty(cause.getMessage())) {
                if (builder.length() > 0) {
                    builder.append('\n');
                }
                builder.append(cause.getMessage());
            }
            cause = cause.getCause();
        }
        String asString = error.toString();
        if (!TextUtils.isEmpty(asString)) {
            if (builder.length() > 0) {
                builder.append('\n');
            }
            builder.append(asString);
        }
        return builder.length() == 0 ? "Bilinmeyen hata" : builder.toString();
    }

    private static String platformLoginMessage(String platform) {
        if ("Instagram".equals(platform)) {
            return "Instagram girişsiz yanıt vermedi. Gönderi gizli, kısıtlı veya oturum çerezi istiyor olabilir. Uygulama içindeki Instagram ekranında giriş yapıp videoyu açarak tekrar deneyebilirsiniz.";
        }
        if ("Facebook".equals(platform)) {
            return "Facebook girişsiz yanıt vermedi. Video gizli, kısıtlı veya oturum çerezi istiyor olabilir. Uygulama içindeki Facebook ekranında giriş yapıp videoyu açarak tekrar deneyebilirsiniz.";
        }
        return "Girişsiz indirme başarısız oldu. İçerik gizli, kısıtlı veya oturum çerezi istiyor olabilir. Uygulama içindeki platform ekranında giriş yapıp tekrar deneyebilirsiniz.";
    }

    private static String instagramPublicExtractorMessage() {
        return "Instagram public video yanıtı alınamadı. Video herkese açık olsa bile Instagram bazen boş medya yanıtı döndürebiliyor. İndirme motorunu güncelleyip tekrar deneyin; devam ederse linki Instagram ekranında açıp Videoyu indir butonunu kullanın.";
    }

    private static boolean isInstagramPublicExtractorError(String platform, String message) {
        if (!"Instagram".equals(platform) || TextUtils.isEmpty(message)) {
            return false;
        }
        String lower = message.toLowerCase(Locale.US);
        return lower.contains("instagram") && lower.contains("empty media response");
    }

    private static boolean isPlatformLoginError(String platform, String message) {
        if (TextUtils.isEmpty(message)) {
            return false;
        }
        String lower = message.toLowerCase(Locale.US);
        String normalizedPlatform = platform == null ? "" : platform.toLowerCase(Locale.US);
        boolean explicitLogin = lower.contains("login required")
                || lower.contains("login_required")
                || lower.contains("requires login")
                || lower.contains("require login")
                || lower.contains("you need to log in")
                || lower.contains("must be logged in")
                || lower.contains("not logged in")
                || lower.contains("sign in to")
                || lower.contains("please sign in")
                || lower.contains("checkpoint_required")
                || lower.contains("private")
                || lower.contains("only confirmed followers");
        boolean cookieHint = lower.contains("--cookies")
                || lower.contains("cookies");
        if ("instagram".equals(normalizedPlatform)) {
            return lower.contains("instagram") && explicitLogin;
        }
        if ("facebook".equals(normalizedPlatform)) {
            return lower.contains("facebook") && (explicitLogin || cookieHint);
        }
        return !TextUtils.isEmpty(normalizedPlatform)
                && lower.contains(normalizedPlatform)
                && (explicitLogin || cookieHint);
    }

    private static String stripOutdatedWarning(String message) {
        String normalized = message.replace("\r\n", "\n").replace('\r', '\n').trim();
        String lower = normalized.toLowerCase(Locale.US);
        int warningIndex = lower.indexOf("warning: your yt-dlp version");
        if (warningIndex < 0) {
            return normalized;
        }

        int errorIndex = lower.indexOf("error:", warningIndex);
        if (errorIndex > warningIndex) {
            return (normalized.substring(0, warningIndex) + normalized.substring(errorIndex)).trim();
        }
        return "İndirme motoru eski sürüm uyarısı verdi. Uygulama açılışta otomatik güncelleme dener; tekrar deneyin veya indirme motorunu güncelle butonunu kullanın.";
    }

    private static final class PlaylistEntry {
        final int index;
        final String title;
        final String id;
        final String url;

        PlaylistEntry(int index, String title, String id, String url) {
            this.index = index;
            this.title = title;
            this.id = id;
            this.url = url;
        }
    }

    private static final class InstagramPublicMedia {
        final String videoUrl;
        final String title;
        final String referer;
        final String shortcode;

        InstagramPublicMedia(String videoUrl, String title, String referer, String shortcode) {
            this.videoUrl = videoUrl;
            this.title = title;
            this.referer = referer;
            this.shortcode = shortcode;
        }
    }

    private static final class InstagramWebSession {
        final Map<String, String> cookies;
        final String lsd;

        InstagramWebSession(Map<String, String> cookies, String lsd) {
            this.cookies = cookies == null ? new LinkedHashMap<>() : cookies;
            this.lsd = lsd == null ? "" : lsd;
        }
    }

    private static final class PlaylistDownloadResult {
        final List<File> downloadedFiles;
        final Uri firstUri;
        final int successCount;
        final int failedCount;
        final int totalCount;

        PlaylistDownloadResult(List<File> downloadedFiles, Uri firstUri, int successCount, int failedCount, int totalCount) {
            this.downloadedFiles = downloadedFiles;
            this.firstUri = firstUri;
            this.successCount = successCount;
            this.failedCount = failedCount;
            this.totalCount = totalCount;
        }
    }

    private static final class PlaylistStatusRow {
        final int index;
        final LinearLayout container;
        final TextView titleView;
        final TextView statusView;
        final ProgressBar progressBar;
        final CheckBox includeBox;
        final Button actionButton;
        File file;

        PlaylistStatusRow(int index, LinearLayout container, TextView titleView, TextView statusView, ProgressBar progressBar, CheckBox includeBox, Button actionButton) {
            this.index = index;
            this.container = container;
            this.titleView = titleView;
            this.statusView = statusView;
            this.progressBar = progressBar;
            this.includeBox = includeBox;
            this.actionButton = actionButton;
        }
    }

    private static final class AnimatedUpdateCardView extends FrameLayout {
        private final AppThemeOption theme;
        private final Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint bandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final RectF rect = new RectF();
        private final float density;
        private LinearGradient cachedGradient;
        private int cachedWidth;
        private int cachedHeight;

        AnimatedUpdateCardView(Context context, AppThemeOption theme) {
            super(context);
            this.theme = theme;
            this.density = context.getResources().getDisplayMetrics().density;
            setWillNotDraw(false);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(dpLocal(1));
            borderPaint.setColor(theme.borderColor);
            bandPaint.setStyle(Paint.Style.STROKE);
            bandPaint.setStrokeWidth(dpLocal(1.35f));
            bandPaint.setColor(Color.argb(isDark(theme) ? 46 : 30, Color.red(theme.accentColor), Color.green(theme.accentColor), Color.blue(theme.accentColor)));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int width = getWidth();
            int height = getHeight();
            if (width <= 0 || height <= 0) {
                super.onDraw(canvas);
                return;
            }

            float inset = dpLocal(2);
            float radius = dpLocal(18);
            rect.set(inset, inset, width - inset, height - inset);
            if (cachedGradient == null || cachedWidth != width || cachedHeight != height) {
                cachedWidth = width;
                cachedHeight = height;
                int top = mix(theme.surfaceColor, theme.accentColor, isDark(theme) ? 0.17f : 0.08f);
                int mid = theme.surfaceColor;
                int bottom = mix(theme.surfaceAltColor, theme.accentColor, isDark(theme) ? 0.10f : 0.05f);
                cachedGradient = new LinearGradient(0, 0, width, height, new int[]{top, mid, bottom}, new float[]{0f, 0.56f, 1f}, Shader.TileMode.CLAMP);
            }

            fillPaint.setShader(cachedGradient);
            fillPaint.setShadowLayer(dpLocal(18), 0, dpLocal(8), Color.argb(isDark(theme) ? 120 : 42, 0, 0, 0));
            canvas.drawRoundRect(rect, radius, radius, fillPaint);
            fillPaint.setShader(null);
            fillPaint.clearShadowLayer();

            float step = dpLocal(34);
            float travel = (System.currentTimeMillis() % 1800L) / 1800f * step;
            canvas.save();
            canvas.clipRect(rect);
            for (float x = -height + travel; x < width + height; x += step) {
                canvas.drawLine(x, height, x + height, 0, bandPaint);
            }
            canvas.restore();

            canvas.drawRoundRect(rect, radius, radius, borderPaint);
            super.onDraw(canvas);
            postInvalidateOnAnimation();
        }

        private float dpLocal(float value) {
            return value * density;
        }
    }

    private static final class AnimatedUpdateProgressView extends View {
        private final AppThemeOption theme;
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint stripePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final RectF rect = new RectF();
        private final float density;
        private int progress;
        private boolean indeterminate;

        AnimatedUpdateProgressView(Context context, AppThemeOption theme) {
            super(context);
            this.theme = theme;
            this.density = context.getResources().getDisplayMetrics().density;
            stripePaint.setStyle(Paint.Style.STROKE);
            stripePaint.setStrokeWidth(dpLocal(2));
            stripePaint.setColor(Color.argb(isDark(theme) ? 84 : 92, 255, 255, 255));
        }

        void setProgressPercent(int progress, boolean indeterminate) {
            this.progress = Math.max(0, Math.min(100, progress));
            this.indeterminate = indeterminate;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int width = getWidth();
            int height = getHeight();
            if (width <= 0 || height <= 0) {
                return;
            }

            float radius = height / 2f;
            rect.set(0, 0, width, height);
            paint.setShader(null);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(mix(theme.surfaceAltColor, theme.borderColor, isDark(theme) ? 0.50f : 0.35f));
            canvas.drawRoundRect(rect, radius, radius, paint);

            float phase = (System.currentTimeMillis() % 1300L) / 1300f;
            if (indeterminate) {
                float progressWidth = width * 0.42f;
                float start = phase * (width + progressWidth) - progressWidth;
                rect.set(start, 0, Math.min(width, start + progressWidth), height);
            } else {
                float progressWidth = Math.max(radius, width * (progress / 100f));
                rect.set(0, 0, progressWidth, height);
            }

            int bright = mix(theme.accentColor, Color.WHITE, isDark(theme) ? 0.18f : 0.28f);
            paint.setShader(new LinearGradient(rect.left, 0, Math.max(rect.left + 1, rect.right), 0,
                    new int[]{theme.accentColor, bright, theme.accentColor},
                    new float[]{0f, 0.55f, 1f},
                    Shader.TileMode.CLAMP));
            canvas.drawRoundRect(rect, radius, radius, paint);
            paint.setShader(null);

            canvas.save();
            canvas.clipRect(rect);
            float step = dpLocal(20);
            float travel = phase * step;
            for (float x = -height + travel; x < width + height; x += step) {
                canvas.drawLine(x, height, x + height, 0, stripePaint);
            }
            canvas.restore();

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(dpLocal(1));
            paint.setColor(theme.borderColor);
            rect.set(0.5f, 0.5f, width - 0.5f, height - 0.5f);
            canvas.drawRoundRect(rect, radius, radius, paint);
            postInvalidateOnAnimation();
        }

        private float dpLocal(float value) {
            return value * density;
        }
    }

    private static boolean isDark(AppThemeOption theme) {
        return luminance(theme.backgroundColor) < 0.35;
    }

    private static int mix(int base, int overlay, float overlayWeight) {
        float clamped = Math.max(0f, Math.min(1f, overlayWeight));
        float baseWeight = 1f - clamped;
        return Color.rgb(
                Math.round(Color.red(base) * baseWeight + Color.red(overlay) * clamped),
                Math.round(Color.green(base) * baseWeight + Color.green(overlay) * clamped),
                Math.round(Color.blue(base) * baseWeight + Color.blue(overlay) * clamped)
        );
    }

    private static final class DownloadOption {
        final String label;
        final int height;
        final boolean audio;

        private DownloadOption(String label, int height, boolean audio) {
            this.label = label;
            this.height = height;
            this.audio = audio;
        }

        static DownloadOption video(String label, int height) {
            return new DownloadOption(label, height, false);
        }

        static DownloadOption audio(String label) {
            return new DownloadOption(label, 0, true);
        }
    }

    private static final class QueuedDownload {
        final String url;
        final String platform;
        final DownloadOption option;
        final boolean playlistMode;

        QueuedDownload(String url, String platform, DownloadOption option, boolean playlistMode) {
            this.url = url;
            this.platform = platform;
            this.option = option;
            this.playlistMode = playlistMode;
        }
    }

    private static final class CookieLine {
        final String domain;
        final String name;
        final String value;

        CookieLine(String domain, String name, String value) {
            this.domain = domain;
            this.name = name;
            this.value = value;
        }
    }

    private static final class SocialPlatform {
        final String name;
        final String homeUrl;
        final int iconRes;
        final int accentColor;

        SocialPlatform(String name, String homeUrl, int iconRes, int accentColor) {
            this.name = name;
            this.homeUrl = homeUrl;
            this.iconRes = iconRes;
            this.accentColor = accentColor;
        }
    }

    private static final class AppThemeOption {
        final String key;
        final String trName;
        final String enName;
        final String trSubtitle;
        final String enSubtitle;
        final int accentColor;
        final int backgroundColor;
        final int surfaceColor;
        final int surfaceAltColor;
        final int borderColor;
        final int textColor;
        final int mutedColor;
        final int onAccentColor;

        AppThemeOption(String key, String trName, String enName, String trSubtitle, String enSubtitle,
                       int accentColor, int backgroundColor, int surfaceColor, int surfaceAltColor, int borderColor,
                       int textColor, int mutedColor, int onAccentColor) {
            this.key = key;
            this.trName = trName;
            this.enName = enName;
            this.trSubtitle = trSubtitle;
            this.enSubtitle = enSubtitle;
            this.accentColor = accentColor;
            this.backgroundColor = backgroundColor;
            this.surfaceColor = surfaceColor;
            this.surfaceAltColor = surfaceAltColor;
            this.borderColor = borderColor;
            this.textColor = textColor;
            this.mutedColor = mutedColor;
            this.onAccentColor = onAccentColor;
        }
    }

    private static final class RefreshMode {
        final float rate;
        final int modeId;

        RefreshMode(float rate, int modeId) {
            this.rate = rate;
            this.modeId = modeId;
        }
    }

    private interface SettingValueHandler {
        void onValue(String value);
    }

    private interface ValueLabelFormatter {
        String label(String value);
    }

    private interface SwitchValueHandler {
        void onValue(boolean value);
    }

    private static final class AppUpdateInfo {
        final String releaseName;
        final String tagName;
        final String latestVersion;
        final String currentVersion;
        final String htmlUrl;
        final String apkUrl;
        final boolean newer;
        final boolean mandatory;

        AppUpdateInfo(String releaseName, String tagName, String latestVersion, String currentVersion, String htmlUrl, String apkUrl, boolean newer, boolean mandatory) {
            this.releaseName = releaseName;
            this.tagName = tagName;
            this.latestVersion = latestVersion;
            this.currentVersion = currentVersion;
            this.htmlUrl = htmlUrl;
            this.apkUrl = apkUrl;
            this.newer = newer;
            this.mandatory = mandatory;
        }

        String latestLabel() {
            if (!TextUtils.isEmpty(tagName)) {
                return tagName;
            }
            if (!TextUtils.isEmpty(latestVersion)) {
                return latestVersion;
            }
            return releaseName;
        }
    }

    private static final class LicenseResult {
        final boolean active;
        final String status;
        final String message;
        final String owner;
        final String expiresAt;
        final String licenseKey;
        final String email;
        final String requestId;
        final String boundDeviceId;
        final String boundDeviceLabel;
        final long nextDeviceChangeAt;

        LicenseResult(boolean active, String status, String message, String owner, String expiresAt, String licenseKey, String email, String requestId, String boundDeviceId, String boundDeviceLabel, long nextDeviceChangeAt) {
            this.active = active;
            this.status = status;
            this.message = message;
            this.owner = owner;
            this.expiresAt = expiresAt;
            this.licenseKey = licenseKey;
            this.email = email;
            this.requestId = requestId;
            this.boundDeviceId = boundDeviceId;
            this.boundDeviceLabel = boundDeviceLabel;
            this.nextDeviceChangeAt = nextDeviceChangeAt;
        }
    }

    private static final class AuthSession {
        final String email;
        final String idToken;
        final String refreshToken;
        final String localId;
        final long expiresAt;

        AuthSession(String email, String idToken, String refreshToken, String localId, long expiresAt) {
            this.email = email;
            this.idToken = idToken;
            this.refreshToken = refreshToken;
            this.localId = localId;
            this.expiresAt = expiresAt;
        }
    }

    private static final class FirestoreResponse {
        final int code;
        final String body;

        FirestoreResponse(int code, String body) {
            this.code = code;
            this.body = body;
        }
    }

    private static final class UpdateResult {
        final String message;

        UpdateResult(String message) {
            this.message = message;
        }
    }
}
