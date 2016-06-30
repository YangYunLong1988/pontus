package com.snowstore.pontus.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

@Service
public class GridfsService {
	@Autowired
	private GridFsTemplate gridFsTemplate;

	/**
	 * 获取文件内容
	 */
	public GridFSDBFile findContent(String objectId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(objectId)));
		return gridFsTemplate.findOne(query);
	}

	/**
	 * 保存文件（将文件保存到MongoDB相关信息保存到Oracle）
	 */
	public GridFSFile save(String fileName, String fileType, InputStream input) {
		GridFSFile gridFile = gridFsTemplate.store(input, fileName, fileType);
		return gridFile;
	}

	public String saveAndGetObjectId(String fileName, String fileType, InputStream input) {
		return save(fileName, fileType, input).getId().toString();
	}

	/**
	 * 获取byte[]
	 * 
	 * @param objectId
	 * @return
	 * @throws IOException
	 */
	public byte[] getByteContent(String objectId) throws IOException {
		GridFSDBFile gridFile = findContent(objectId);
		return IOUtils.toByteArray(gridFile.getInputStream());
	}

	/**
	 * 获取base64编码
	 * 
	 * @param objectId
	 * @return
	 * @throws IOException
	 */
	public String getBase64Content(String objectId) throws IOException {
		return Base64Utils.encodeToString(getByteContent(objectId));
	}
}
