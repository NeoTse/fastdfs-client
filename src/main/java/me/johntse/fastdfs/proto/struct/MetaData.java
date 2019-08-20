package me.johntse.fastdfs.proto.struct;

/**
 * 元数据，用于描述文件元数据信息.
 *
 * @author johntse
 * @since 0.1.0
 */
public class MetaData {
    private String name;
    private String value;

    public MetaData() {

    }

    public MetaData(String name) {
        this.name = name;
        this.value = "";
    }

    public MetaData(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj != null && obj.getClass() == getClass()) {
            MetaData other = (MetaData) obj;

            return other.name != null && other.name.equals(name)
                    && ((other.value != null && value != null && other.value.equals(value))
                    || (other.value == null && value == null));
        }

        return false;
    }

    @Override
    public String toString() {
        return "MetaData [name=" + name + ", value=" + value + "]";
    }
}
