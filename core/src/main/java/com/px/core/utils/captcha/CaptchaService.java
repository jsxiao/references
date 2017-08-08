package com.px.core.utils.captcha;

import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

public class CaptchaService {

	private static ImageCaptchaService instance = new DefaultManageableImageCaptchaService(
			new FastHashMapCaptchaStore(), new RdImageEngine(), 180, 100000, 75000);

	private CaptchaService(){}
	
	public static ImageCaptchaService getInstance() {
		return instance;
	}
}
