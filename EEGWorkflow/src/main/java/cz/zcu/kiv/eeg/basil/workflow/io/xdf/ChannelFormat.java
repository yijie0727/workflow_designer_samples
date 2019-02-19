package cz.zcu.kiv.eeg.basil.workflow.io.xdf;

/**
 * Created by Tomas Prokop on 28.01.2019.
 */
public enum ChannelFormat {
    int8, int16, int32, int64, float32, double64, string;

    public static int getBytesCount(ChannelFormat format) {
        int cnt;
        switch (format) {
            case int8:
                cnt = 1;
                break;
            case int16:
                cnt = 2;
                break;
            case int32:
            case float32:
                cnt = 4;
                break;
            case int64:
            case double64:
                cnt = 8;
                break;
            default:
                cnt = -1;
                break;
        }

        return cnt;
    }
}
