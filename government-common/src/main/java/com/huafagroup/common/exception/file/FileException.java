package com.huafagroup.common.exception.file;

import com.huafagroup.common.exception.BaseException;

/**
 * 文件信息异常类
 *
 * @author huafagroup
 */
public class FileException extends BaseException {
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }

}
