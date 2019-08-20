package me.johntse.fastdfs.proto;

import me.johntse.fastdfs.proto.base.Codec;
import me.johntse.fastdfs.proto.base.Fields;
import me.johntse.fastdfs.proto.storage.internal.DeleteFileParameters;
import me.johntse.fastdfs.proto.storage.internal.GetMetaDataParameters;
import me.johntse.fastdfs.proto.storage.internal.SetMetaDataParameters;
import me.johntse.fastdfs.proto.storage.internal.UploadFileParameters;
import me.johntse.fastdfs.proto.struct.GroupState;
import me.johntse.fastdfs.proto.struct.MetaData;
import me.johntse.fastdfs.proto.struct.StorageNode;
import me.johntse.fastdfs.proto.struct.StorageNodeLite;
import me.johntse.fastdfs.proto.struct.StoragePath;
import me.johntse.fastdfs.proto.struct.StorageState;
import me.johntse.fastdfs.proto.tracker.internal.FetchOneStorageParameters;
import me.johntse.fastdfs.proto.tracker.internal.GetOneStorageWithGroupParameters;
import me.johntse.fastdfs.proto.tracker.internal.GetUpdateStorageParameters;
import me.johntse.fastdfs.util.ByteUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * fast DFS Server Mock.
 *
 * @author johntse
 * @since 0.1.0
 */
public class ServerMock {
    private List<GroupState> groupStates;
    private List<StorageState> storageStates;
    private Set<MetaData> metaDataSet;

    private StorageNode storageNode;
    private StorageNodeLite storageNodeLite;
    private StoragePath storagePath;

    private String filePath;
    private String fileContent;
    private String downloadFileContent;
    private boolean isFileDelete;

    public ServerMock() {
        storagePath = new StoragePath("G", "this/is/your/file/path");
        storageNode = new StorageNode("分组A", "127.0.0.1", 80, (byte) 2);
        storageNodeLite = new StorageNodeLite();
        storageNodeLite.setIp("127.0.0.1");
        storageNodeLite.setPort(80);

        isFileDelete = false;
        filePath = "";

        downloadFileContent = "Hello, idiot human! I'm the one!!";

        initGroupStates();
        initStorageStates();
        initMetaDataSet();
    }

    public InputStream response(ByteArrayOutputStream request, Charset charset) {
        byte[] requestContent = request.toByteArray();

        InputStream response = null;
        try {
            Header requestHeader = Codec.decodeToInstance(requestContent, Header.class, charset);
            byte[] parameterContent = null;
            if (requestHeader.getContentLength() > 0) {
                parameterContent = new byte[(int) requestHeader.getContentLength()];
                System.arraycopy(requestContent, Header.HEAD_LENGTH,
                        parameterContent, 0, parameterContent.length);
            }

            switch (requestHeader.getType()) {
                case ProtocolConstants.TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ONE:
                    response = responseFetchOneStorage(parameterContent, charset);
                    break;
                case ProtocolConstants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE:
                    response = responseOneStorage(parameterContent, charset);
                    break;
                case ProtocolConstants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE:
                    response = responseOneStorageWithGroup(parameterContent, charset);
                    break;
                case ProtocolConstants.TRACKER_PROTO_CMD_SERVICE_QUERY_UPDATE:
                    response = responseUpdateStorage(parameterContent, charset);
                    break;
                case ProtocolConstants.TRACKER_PROTO_CMD_SERVER_LIST_GROUP:
                    response = responseListGroups(parameterContent, charset);
                    break;
                case ProtocolConstants.TRACKER_PROTO_CMD_SERVER_LIST_STORAGE:
                    response = responseListStorages(parameterContent, charset);
                    break;
                case ProtocolConstants.STORAGE_PROTO_CMD_DELETE_FILE:
                    response = responseDeleteFile(parameterContent, charset);
                    break;
                case ProtocolConstants.STORAGE_PROTO_CMD_SET_METADATA:
                    response = responseSetMetaData(parameterContent, charset);
                    break;
                case ProtocolConstants.STORAGE_PROTO_CMD_GET_METADATA:
                    response = responseGetMetaData(parameterContent, charset);
                    break;
                case ProtocolConstants.STORAGE_PROTO_CMD_UPLOAD_FILE:
                case ProtocolConstants.STORAGE_PROTO_CMD_UPLOAD_APPENDER_FILE:
                    response = responseUploadFile(parameterContent, charset);
                    break;
                case ProtocolConstants.STORAGE_PROTO_CMD_DOWNLOAD_FILE:
                    response = responseDownloadFile(parameterContent, charset);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Request.");
            }

            return response;
        } catch (IllegalAccessException | InstantiationException e) {
            return response;
        }
    }

    public StorageNode getStorageNode() {
        return storageNode;
    }

    public StorageNodeLite getStorageNodeLite() {
        return storageNodeLite;
    }

    public List<GroupState> getGroupStates() {
        return Collections.unmodifiableList(groupStates);
    }

    public List<StorageState> getStorageStates() {
        return Collections.unmodifiableList(storageStates);
    }

    public boolean isFileDelete() {
        return isFileDelete;
    }

    public String getFilePath() {
        return filePath;
    }

    public Set<MetaData> getMetaDataSet() {
        return metaDataSet;
    }

    public String getFileContent() {
        return fileContent;
    }

    public StoragePath getStoragePath() {
        return storagePath;
    }

    public String getDownloadFileContent() {
        return downloadFileContent;
    }

    private InputStream generateResponse(byte[] body, Charset charset) {
        Header header = new Header(body.length, ProtocolConstants.FDFS_PROTO_CMD_RESP);

        byte[] headBytes = Codec.encodeToBytes(header, charset);

        byte[] result = new byte[headBytes.length + body.length];
        int offset = 0;
        System.arraycopy(headBytes, 0, result, offset, headBytes.length);
        offset += headBytes.length;
        System.arraycopy(body, 0, result, offset, body.length);

        return new ByteArrayInputStream(result);
    }

    private InputStream responseDeleteFile(byte[] content, Charset charset)
            throws InstantiationException, IllegalAccessException {
        isFileDelete = true;
        DeleteFileParameters parameters = Codec.decodeToInstance(content, DeleteFileParameters.class, charset);

        filePath = parameters.getFilePath();
        return generateResponse(ByteUtils.EMPTY_BYTES, charset);
    }

    private InputStream responseUploadFile(byte[] content, Charset charset)
            throws InstantiationException, IllegalAccessException {
        UploadFileParameters parameters = Codec.decodeToInstance(content, UploadFileParameters.class, charset);

        Fields.FieldCollection collection = Fields.getInstance().getCollection(parameters.getClass());
        int parametersLength = collection.getTotalFixedFieldSize();

        byte[] uploadFileContent = new byte[content.length - parametersLength];
        System.arraycopy(content, parametersLength, uploadFileContent, 0, uploadFileContent.length);
        fileContent = new String(uploadFileContent, charset);

        return generateResponse(Codec.encodeToBytes(storagePath, charset), charset);
    }

    private InputStream responseDownloadFile(byte[] content, Charset charset) {
        return generateResponse(downloadFileContent.getBytes(charset), charset);
    }

    private InputStream responseSetMetaData(byte[] content, Charset charset)
            throws InstantiationException, IllegalAccessException {
        SetMetaDataParameters parameters = Codec.decodeToInstance(content, SetMetaDataParameters.class, charset);
        filePath = parameters.getFilePath();
        metaDataSet = parameters.getMetaDataSet();

        return generateResponse(ByteUtils.EMPTY_BYTES, charset);
    }

    private InputStream responseGetMetaData(byte[] content, Charset charset)
            throws InstantiationException, IllegalAccessException {
        GetMetaDataParameters parameters = Codec.decodeToInstance(content, GetMetaDataParameters.class, charset);
        filePath = parameters.getFilePath();
        metaDataSet = new HashSet<>();
        metaDataSet.add(new MetaData("response", "OK!"));
        metaDataSet.add(new MetaData("name", "Doctor who"));

        byte[] resultByte = Codec.encodeMetaToBytes(metaDataSet, charset);
        return generateResponse(resultByte, charset);
    }

    private InputStream responseFetchOneStorage(byte[] content, Charset charset)
            throws InstantiationException, IllegalAccessException {
        FetchOneStorageParameters parameters =
                Codec.decodeToInstance(content, FetchOneStorageParameters.class, charset);

        storageNodeLite.setGroupName(parameters.getGroupName());
        byte[] resultByte = Codec.encodeToBytes(storageNodeLite, charset);
        return generateResponse(resultByte, charset);
    }

    private InputStream responseOneStorage(byte[] content, Charset charset) {
        byte[] resultByte = Codec.encodeToBytes(storageNode, charset);
        return generateResponse(resultByte, charset);
    }

    private InputStream responseOneStorageWithGroup(byte[] content, Charset charset)
            throws InstantiationException, IllegalAccessException {
        GetOneStorageWithGroupParameters parameters =
                Codec.decodeToInstance(content, GetOneStorageWithGroupParameters.class, charset);
        storageNode.setGroupName(parameters.getGroupName());
        byte[] resultByte = Codec.encodeToBytes(storageNode, charset);
        return generateResponse(resultByte, charset);
    }

    private InputStream responseUpdateStorage(byte[] content, Charset charset)
            throws InstantiationException, IllegalAccessException {
        GetUpdateStorageParameters parameters =
                Codec.decodeToInstance(content, GetUpdateStorageParameters.class, charset);
        storageNodeLite.setGroupName(parameters.getGroupName());
        byte[] resultByte = Codec.encodeToBytes(storageNodeLite, charset);
        return generateResponse(resultByte, charset);
    }

    private InputStream responseListGroups(byte[] content, Charset charset) {
        List<byte[]> results = new ArrayList<>();

        int total = 0;
        for (GroupState groupState : groupStates) {
            byte[] bytes = Codec.encodeToBytes(groupState, charset);
            total += bytes.length;

            results.add(bytes);
        }

        assert total / results.size() == results.get(0).length;


        byte[] resultByte = new byte[total];
        int offset = 0;

        for (byte[] result : results) {
            System.arraycopy(result, 0, resultByte, offset, result.length);
            offset += result.length;
        }

        assert offset == total;

        return generateResponse(resultByte, charset);
    }

    private InputStream responseListStorages(byte[] content, Charset charset) {
        List<byte[]> results = new ArrayList<>();

        int total = 0;
        for (StorageState storageState : storageStates) {
            byte[] bytes = Codec.encodeToBytes(storageState, charset);
            total += bytes.length;

            results.add(bytes);
        }

        assert total / results.size() == results.get(0).length;


        byte[] resultByte = new byte[total];
        int offset = 0;

        for (byte[] result : results) {
            System.arraycopy(result, 0, resultByte, offset, result.length);
            offset += result.length;
        }

        assert offset == total;

        return generateResponse(resultByte, charset);
    }

    private void initGroupStates() {
        groupStates = new ArrayList<>();
        GroupState groupState = new GroupState();

        groupState.setGroupName("Group1");
        groupState.setTotal(1000);
        groupState.setFree(900);
        groupState.setTrunkFree(10);
        groupState.setStorageCount(3);
        groupState.setStorageServerPort(22122);
        groupState.setStorageHttpServerPort(80);
        groupState.setActiveStorageCount(2);
        groupState.setCurrentWriteStorageCount(0);
        groupState.setStorePathPerStorage(2);
        groupState.setSubDirPerStorePath(256);
        groupState.setCurrentTrunkFileId(1);
        groupStates.add(groupState);

        groupState = new GroupState();

        groupState.setGroupName("Group2");
        groupState.setTotal(900);
        groupState.setFree(850);
        groupState.setTrunkFree(9);
        groupState.setStorageCount(2);
        groupState.setStorageServerPort(22120);
        groupState.setStorageHttpServerPort(8080);
        groupState.setActiveStorageCount(3);
        groupState.setCurrentWriteStorageCount(1);
        groupState.setStorePathPerStorage(3);
        groupState.setSubDirPerStorePath(256);
        groupState.setCurrentTrunkFileId(2);
        groupStates.add(groupState);
    }

    private void initStorageStates() {
        storageStates = new ArrayList<>();

        StorageState storageState = new StorageState();
        storageState.setId("10000");
        storageState.setIpAddress("10.0.14.1");
        storageState.setHttpDomainName("");
        storageState.setSrcIpAddress("10.0.14.1");
        storageState.setVersion("1.0");
        storageState.setJoinTime(new Date());
        storageState.setUpTime(new Date());
        storageState.setTotal(1000);
        storageState.setFree(500);
        storageState.setUploadPriority(10);
        storageState.setStorePathCount(2);
        storageState.setSubDirPerStorePath(256);
        storageState.setCurrentWriteStorePath(0);
        storageState.setStorageServerPort(21222);
        storageState.setStorageHttpPort(80);
        storageState.setConnectionAllocCount(0);
        storageState.setConnectionCurrentCount(0);
        storageState.setConnectionMaxCount(10);
        storageState.setTotalUploadCount(0);
        storageState.setSuccessUploadCount(0);
        storageState.setTotalAppendCount(0);
        storageState.setSuccessAppendCount(0);
        storageState.setTotalModifyCount(0);
        storageState.setSuccessModifyCount(0);
        storageState.setTotalTruncateCount(0);
        storageState.setSuccessTruncateCount(0);
        storageState.setTotalSetMetaCount(0);
        storageState.setSuccessSetMetaCount(0);
        storageState.setTotalDeleteCount(0);
        storageState.setSuccessDeleteCount(0);
        storageState.setTotalDownloadCount(0);
        storageState.setSuccessDownloadCount(0);
        storageState.setTotalGetMetaCount(0);
        storageState.setSuccessGetMetaCount(0);
        storageState.setTotalCreateLinkCount(0);
        storageState.setSuccessCreateLinkCount(0);
        storageState.setTotalDeleteLinkCount(0);
        storageState.setSuccessDeleteLinkCount(0);
        storageState.setTotalUploadBytes(0);
        storageState.setSuccessUploadBytes(0);
        storageState.setTotalAppendBytes(0);
        storageState.setSuccessAppendBytes(0);
        storageState.setTotalModifyBytes(0);
        storageState.setSuccessModifyBytes(0);
        storageState.setTotalDownloadBytes(0);
        storageState.setSuccessDownloadBytes(0);
        storageState.setTotalSyncInBytes(0);
        storageState.setSuccessSyncInBytes(0);
        storageState.setTotalSyncOutBytes(0);
        storageState.setSuccessSyncOutBytes(0);
        storageState.setTotalFileOpenCount(0);
        storageState.setSuccessFileOpenCount(0);
        storageState.setTotalFileReadCount(0);
        storageState.setSuccessFileReadCount(0);
        storageState.setTotalFileWriteCount(0);
        storageState.setSuccessFileWriteCount(0);
        storageState.setLastSourceUpdate(new Date());
        storageState.setLastSyncUpdate(new Date());
        storageState.setLastSyncedTimestamp(new Date());
        storageState.setLastHeartBeatTime(new Date());
        storageState.setTrunkServer(false);

        storageStates.add(storageState);

        storageState = new StorageState();
        storageState.setId("10001");
        storageState.setIpAddress("10.0.14.2");
        storageState.setHttpDomainName("");
        storageState.setSrcIpAddress("10.0.14.2");
        storageState.setVersion("1.0");
        storageState.setJoinTime(new Date());
        storageState.setUpTime(new Date());
        storageState.setTotal(900);
        storageState.setFree(600);
        storageState.setUploadPriority(20);
        storageState.setStorePathCount(3);
        storageState.setSubDirPerStorePath(255);
        storageState.setCurrentWriteStorePath(1);
        storageState.setStorageServerPort(21221);
        storageState.setStorageHttpPort(8080);
        storageState.setConnectionAllocCount(1);
        storageState.setConnectionCurrentCount(1);
        storageState.setConnectionMaxCount(10);
        storageState.setTotalUploadCount(1);
        storageState.setSuccessUploadCount(1);
        storageState.setTotalAppendCount(1);
        storageState.setSuccessAppendCount(1);
        storageState.setTotalModifyCount(1);
        storageState.setSuccessModifyCount(1);
        storageState.setTotalTruncateCount(1);
        storageState.setSuccessTruncateCount(1);
        storageState.setTotalSetMetaCount(1);
        storageState.setSuccessSetMetaCount(1);
        storageState.setTotalDeleteCount(1);
        storageState.setSuccessDeleteCount(1);
        storageState.setTotalDownloadCount(1);
        storageState.setSuccessDownloadCount(1);
        storageState.setTotalGetMetaCount(1);
        storageState.setSuccessGetMetaCount(1);
        storageState.setTotalCreateLinkCount(1);
        storageState.setSuccessCreateLinkCount(1);
        storageState.setTotalDeleteLinkCount(1);
        storageState.setSuccessDeleteLinkCount(1);
        storageState.setTotalUploadBytes(1);
        storageState.setSuccessUploadBytes(1);
        storageState.setTotalAppendBytes(1);
        storageState.setSuccessAppendBytes(1);
        storageState.setTotalModifyBytes(1);
        storageState.setSuccessModifyBytes(1);
        storageState.setTotalDownloadBytes(1);
        storageState.setSuccessDownloadBytes(1);
        storageState.setTotalSyncInBytes(1);
        storageState.setSuccessSyncInBytes(1);
        storageState.setTotalSyncOutBytes(1);
        storageState.setSuccessSyncOutBytes(1);
        storageState.setTotalFileOpenCount(1);
        storageState.setSuccessFileOpenCount(1);
        storageState.setTotalFileReadCount(1);
        storageState.setSuccessFileReadCount(1);
        storageState.setTotalFileWriteCount(1);
        storageState.setSuccessFileWriteCount(1);
        storageState.setLastSourceUpdate(new Date());
        storageState.setLastSyncUpdate(new Date());
        storageState.setLastSyncedTimestamp(new Date());
        storageState.setLastHeartBeatTime(new Date());
        storageState.setTrunkServer(true);
        storageStates.add(storageState);
    }

    private void initMetaDataSet() {
        metaDataSet = new HashSet<>();
        metaDataSet.add(new MetaData("author", "John Tse"));
        metaDataSet.add(new MetaData("Company", "FibreHome Co. LTD."));
        metaDataSet.add(new MetaData("Date", "2017-4-12"));
        metaDataSet.add(new MetaData("position", "Senior Programmer"));
    }
}
