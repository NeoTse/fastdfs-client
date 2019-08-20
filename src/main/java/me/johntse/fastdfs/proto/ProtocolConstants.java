package me.johntse.fastdfs.proto;

/**
 * fastDFS通信协议相关常量定义.
 *
 * @author johntse
 * @since 0.1.0
 */
public interface ProtocolConstants {
    byte FDFS_PROTO_CMD_QUIT = 82;
    byte FDFS_PROTO_CMD_ACTIVE_TEST = 111;
    byte FDFS_PROTO_CMD_RESP = 100;

    byte TRACKER_PROTO_CMD_SERVER_LIST_GROUP = 91;
    byte TRACKER_PROTO_CMD_SERVER_LIST_STORAGE = 92;
    byte TRACKER_PROTO_CMD_SERVER_DELETE_STORAGE = 93;
    byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE = 101;
    byte TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ONE = 102;
    byte TRACKER_PROTO_CMD_SERVICE_QUERY_UPDATE = 103;
    byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE = 104;
    byte TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ALL = 105;
    byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ALL = 106;
    byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ALL = 107;
    byte TRACKER_PROTO_CMD_RESP = FDFS_PROTO_CMD_RESP;

    byte STORAGE_PROTO_CMD_UPLOAD_FILE = 11;
    byte STORAGE_PROTO_CMD_DELETE_FILE = 12;
    byte STORAGE_PROTO_CMD_SET_METADATA = 13;
    byte STORAGE_PROTO_CMD_DOWNLOAD_FILE = 14;
    byte STORAGE_PROTO_CMD_GET_METADATA = 15;
    byte STORAGE_PROTO_CMD_UPLOAD_SLAVE_FILE = 21;
    byte STORAGE_PROTO_CMD_QUERY_FILE_INFO = 22;
    byte STORAGE_PROTO_CMD_UPLOAD_APPENDER_FILE = 23;  //create appender file
    byte STORAGE_PROTO_CMD_APPEND_FILE = 24;  //append file
    byte STORAGE_PROTO_CMD_MODIFY_FILE = 34;  //modify appender file
    byte STORAGE_PROTO_CMD_TRUNCATE_FILE = 36;  //truncate appender file
    byte STORAGE_PROTO_CMD_RESP = TRACKER_PROTO_CMD_RESP;


    byte FDFS_STORAGE_STATUS_INIT = 0;
    byte FDFS_STORAGE_STATUS_WAIT_SYNC = 1;
    byte FDFS_STORAGE_STATUS_SYNCING = 2;
    byte FDFS_STORAGE_STATUS_IP_CHANGED = 3;
    byte FDFS_STORAGE_STATUS_DELETED = 4;
    byte FDFS_STORAGE_STATUS_OFFLINE = 5;
    byte FDFS_STORAGE_STATUS_ONLINE = 6;
    byte FDFS_STORAGE_STATUS_ACTIVE = 7;
    byte FDFS_STORAGE_STATUS_NONE = 99;
    byte FDFS_FILE_EXT_NAME_MAX_LEN = 6;
    byte FDFS_FILE_PREFIX_MAX_LEN = 16;
    byte FDFS_FILE_PATH_LEN = 10;
    byte FDFS_FILENAME_BASE64_LENGTH = 27;
    byte FDFS_TRUNK_FILE_INFO_LEN = 16;
    byte ERR_NO_ENOENT = 2;
    byte ERR_NO_EIO = 5;
    byte ERR_NO_EBUSY = 16;
    byte ERR_NO_EINVAL = 22;
    byte ERR_NO_ENOSPC = 28;
    byte ERR_NO_CONNREFUSED = 61;
    byte ERR_NO_EALREADY = 114;
    int FDFS_PROTO_PKG_LEN_SIZE = 8;
    int FDFS_PROTO_CMD_SIZE = 1;
    int FDFS_GROUP_NAME_MAX_LEN = 16;
    int FDFS_IPADDR_SIZE = 16;
    int FDFS_DOMAIN_NAME_MAX_SIZE = 128;
    int FDFS_VERSION_SIZE = 6;
    int FDFS_STORAGE_ID_MAX_SIZE = 16;
    int TRACKER_QUERY_STORAGE_FETCH_BODY_LEN = FDFS_GROUP_NAME_MAX_LEN
            + FDFS_IPADDR_SIZE - 1 + FDFS_PROTO_PKG_LEN_SIZE;
    int TRACKER_QUERY_STORAGE_STORE_BODY_LEN = FDFS_GROUP_NAME_MAX_LEN
            + FDFS_IPADDR_SIZE + FDFS_PROTO_PKG_LEN_SIZE;
    int PROTO_HEADER_CMD_INDEX = FDFS_PROTO_PKG_LEN_SIZE;
    int PROTO_HEADER_STATUS_INDEX = FDFS_PROTO_PKG_LEN_SIZE + 1;
    long INFINITE_FILE_SIZE = 256 * 1024L * 1024 * 1024 * 1024 * 1024L;
    long APPENDER_FILE_SIZE = INFINITE_FILE_SIZE;
    long TRUNK_FILE_MARK_SIZE = 512 * 1024L * 1024 * 1024 * 1024 * 1024L;
    long NORMAL_LOGIC_FILENAME_LENGTH = FDFS_FILE_PATH_LEN + FDFS_FILENAME_BASE64_LENGTH
            + FDFS_FILE_EXT_NAME_MAX_LEN + 1;
    long TRUNK_LOGIC_FILENAME_LENGTH = NORMAL_LOGIC_FILENAME_LENGTH + FDFS_TRUNK_FILE_INFO_LEN;
    String FDFS_RECORD_SEPERATOR = "\u0001";
    String FDFS_FIELD_SEPERATOR = "\u0002";

    enum OperationFlag {
        STORAGE_SET_METADATA_FLAG_OVERWRITE((byte) 'O'), // 覆盖原有的所有元数据
        STORAGE_SET_METADATA_FLAG_MERGE((byte) 'M'); // 如果元数据不存在插入，否则更新

        private byte value;

        OperationFlag(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }
    }
}
