package com.snowstore.pontus.vo;

import java.util.EnumSet;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.snowstore.pontus.service.PontusServiceException;

/**
 * 校验图片格式
 * 
 * @author SM
 * @version 1.0
 * 
 */
public class FileMagicNumberProcessor implements ConstraintValidator<FileMagicNumber, String> {

	private FileType[] fileTypes;

	@Override
	public void initialize(FileMagicNumber constraintAnnotation) {
		this.fileTypes = constraintAnnotation.fileTypes();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isEmpty(value)) {
			return true;
		}
		byte[] header = new byte[28];
		byte[] file = Base64.decodeBase64(value);
		if (file.length < 28) {
			return false;
		}
		for (int i = 0; i < 28; i++) {
			header[i] = file[i];
		}
		String headerString = bytesToHexString(header);
		for (FileType fileType : fileTypes) {
			if (headerString.toUpperCase().startsWith(fileType.getValue())) {
				return true;
			}
		}
		return false;
	}

	public static void validateType(byte[] file, EnumSet<FileType> fileTypeList) {
		// 构建协议对象
		byte[] file1 = file;
		if (file1.length < 28) {
			throw new PontusServiceException("文件格式不正确");
		}
		byte[] header = new byte[28];
		for (int i = 0; i < 28; i++) {
			header[i] = file1[i];
		}
		boolean magicPass = false;
		String headerString = FileMagicNumberProcessor.bytesToHexString(header);
		for (FileType fileType : fileTypeList) {
			if (headerString.toUpperCase().startsWith(fileType.getValue())) {
				magicPass = true;
				break;
			}
		}
		if (!magicPass) {
			throw new PontusServiceException("文件格式不正确");
		}
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static enum FileType {
		/* JEPG */
		JPG("FFD8FF"),
		/* PNG */
		PNG("89504E47"),
		/* GIF */
		GIF("47494638"),
		// bitmap
		BMP("424D"),
		// bitmap
		PDF("255044462D312E");

		private String value = "";

		private FileType(String value) {
			this.setValue(value);
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public static String getType(String changesApplication) {// 取值
			for (FileType type : values()) {
				if (changesApplication.toUpperCase().startsWith(type.getValue())) {
					return type.toString();
				}
			}
			return null;

		}
	}
}
