package com.iot.screen.service.impl;

import com.cloud.common.core.utils.StringUtil;
import com.hidisp.api.cloud.MaterialManager;
import com.hidisp.api.cloud.models.MaterialInfo;
import com.hidisp.api.cloud.models.MaterialResult;
import com.hidisp.api.cloud.models.MaterialStatus;
import com.hidisp.api.cloud.utils.a;
import com.iot.screen.properties.AdvScreenProperties;
import com.iot.screen.service.AdvFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * 广告素材实现类
 *
 * @author cloud
 */
@Service
public class AdvFileServiceImpl implements AdvFileService {

    @Autowired
    private AdvScreenProperties properties;

    /**
     * 获取当前可用的所有广告素材
     *
     * @return 广告素材集合
     */
    @Override
    public List<MaterialInfo> getMaterials() {
        List<MaterialInfo> materialInfos;
        // 获取类型为xx的素材，为*或null则获取全部
        materialInfos = MaterialManager.getInstance().getMaterials(properties.getApiUrl(), properties.getAppKey(), "*");
        return materialInfos;
    }

    /**
     * 检查所选MD5素材的当前状态
     *
     * @param multipartFile 素材对象
     * @return 素材状态集合
     */
    @Override
    public List<MaterialStatus> check(MultipartFile multipartFile) {
        StringBuilder builder = new StringBuilder();
        List<MaterialStatus> materialStatuses = new ArrayList<>();
        try {
            // 转化为file
            File file = copyMultipartToFile(multipartFile);
            // 若file为空则返回空数组
            if (file == null || file.length() == 0) {
                return materialStatuses;
            }
            // 单文件检查，若扩展为多文件，则用逗号隔开
            String md5 = a.a(file);
            // 字符串builder构造字符串
            builder.append(md5).append(":").append(file.length());
            String materials = builder.toString();
            // 开始检查
            materialStatuses = MaterialManager.getInstance().check(properties.getApiUrl(), properties.getAppKey(), materials);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return materialStatuses;
    }

    /**
     * 上传广告素材
     *
     * @param multipartFile 素材对象
     * @return 上传素材结果
     */
    @Override
    public MaterialResult upload(MultipartFile multipartFile) {
        // 上传前先检查素材是否存在
        List<MaterialStatus> statuses = this.check(multipartFile);
        MaterialStatus status = statuses.get(0);
        // 若存在，则return出去不上传
        if (status.getExists()) {
            return new MaterialResult(null, "素材已存在！");
        }
        // 若不存在，执行上传逻辑
        MaterialResult result = new MaterialResult();
        try {
            // 转化为file
            File file = copyMultipartToFile(multipartFile);
            // 若file为空则返回空数组
            if (file == null || file.length() == 0) {
                result.setMd5("上传的素材为空！");
                return result;
            }
            String materials = a.a(file);
            // 开始上传
            result = MaterialManager.getInstance().upload(properties.getApiUrl(), properties.getAppKey(), materials, file.getName(), 0, file, 0L);
        } catch (Exception e) {
            result.setMd5("素材上传失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * MultipartFile转File
     *
     * @param multipartFile MultipartFile对象
     * @return File对象
     * @throws Exception 抛出转化过程中的异常供调用者处理
     */
    public static File copyMultipartToFile(MultipartFile multipartFile) throws Exception {
        // 若文件为空，则返回
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }
        // 若文件源名称为null，则返回
        if (StringUtil.isNullOrEmpty(multipartFile.getOriginalFilename())) {
            return null;
        }
        // 不出现空指针的情况下，转换开始执行
        InputStream stream = multipartFile.getInputStream();
        File file = new File(multipartFile.getOriginalFilename());
        OutputStream os = Files.newOutputStream(file.toPath());

        byte[] bytes = new byte[1024];
        int read;
        while ((read = stream.read(bytes)) != -1) {
            os.write(bytes, 0, read);
        }

        stream.close();
        os.close();
        return file;
    }
}
