package sh.siava.AOSPMods.systemui;

import static de.robv.android.xposed.XposedBridge.hookAllConstructors;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.setObjectField;
import static sh.siava.AOSPMods.XPrefs.Xprefs;

import android.content.Context;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import sh.siava.AOSPMods.AOSPMods;
import sh.siava.AOSPMods.XposedModPack;
import sh.siava.rangesliderpreference.RangeSliderPreference;

@SuppressWarnings("RedundantThrows")
public class NotificationManager extends XposedModPack {
	private static final String listenPackage = AOSPMods.SYSTEM_UI_PACKAGE;

	private Object HeadsUpManager = null;

	private static int HeadupAutoDismissNotificationDecay = -1;

	public NotificationManager(Context context) {
		super(context);
	}

	@Override
	public void updatePrefs(String... Key) {
		try {
			HeadupAutoDismissNotificationDecay = RangeSliderPreference.getValues(Xprefs, "HeadupAutoDismissNotificationDecay", -1).get(0).intValue();
			applyDurations();
		} catch (Throwable ignored) {
		}
	}

	@Override
	public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
		if (!lpparam.packageName.equals(listenPackage)) return;

		Class<?> HeadsUpManagerClass = findClass("com.android.systemui.statusbar.policy.HeadsUpManager", lpparam.classLoader);

		hookAllConstructors(HeadsUpManagerClass, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				HeadsUpManager = param.thisObject;

				applyDurations();
			}
		});

	}

	private void applyDurations() {
		if(HeadsUpManager != null && HeadupAutoDismissNotificationDecay > 0)
		{
			setObjectField(HeadsUpManager, "mMinimumDisplayTime", Math.round(HeadupAutoDismissNotificationDecay/2.5f));
			setObjectField(HeadsUpManager, "mAutoDismissNotificationDecay", HeadupAutoDismissNotificationDecay);
		}
	}

	@Override
	public boolean listensTo(String packageName) {
		return listenPackage.equals(packageName);
	}
}