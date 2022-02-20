package main.java.com.draco.subnetting;

public class Net {

    private int _ip;
    private int _mask;

    public Net(String ip, String mask) {

        String[] ips = ip.split("\\.");
        String[] masks = mask.split("\\.");

        _ip = 0;
        // 192.168.11.12 --> 1100 0000 |24| 1010 1000 |16| 0000 1011 |8| 0000 1100
        byte b = 24;
        for (String s : ips) {
            int aux = Integer.parseInt(s);
            _ip += aux << b;
            b -= 8;
        }

        _mask = 0;
        // 255.255.255.0 --> 1111 1111 |24| 1111 1111 |16| 1111 1111 |8| 0000 0000
        b = 24;
        for (String s : masks) {
            int aux = Integer.parseInt(s);
            _mask += aux << b;
            b -= 8;
        }

    }
    public Net(){
        _ip = 0;
        _mask = 0;
    }

    public int get_ip() {
        return _ip;
    }

    public void set_ip(int _ip) {
        this._ip = _ip;
    }

    public int get_mask() {
        return _mask;
    }

    public void set_mask(int _mask) {
        this._mask = _mask;
    }

    public String getIP() {
        return getString(_ip);
    }

    public String getMask() {
        return getString(_mask);
    }

    public String getNetDir() {
        return getString(netDir());
    }

    public String getBrdDir() {
        return getString(broadcastDir());
    }

    public String getFDir() {
        return getString(firstDir());
    }

    public String getLDir() {
        return getString(lastDir());
    }

    public String toString(boolean opt) {
        if (opt) {
            return " - IP:\t\t\t\t\t\t" + getIP() +
                    "\n - Mask:\t\t\t\t\t" + getMask() +
                    "\n - Net Dir.:\t\t\t\t" + getNetDir() +
                    "\n - Broadcast Dir.:\t\t\t" + getBrdDir() +
                    "\n\t· 1st Available Dir.:\t" + getFDir() +
                    "\n\t· Last Available Dir:\t" + getLDir();
        } else {
            return " - Mask:\t\t\t\t\t" + getMask() +
                    "\n - Net Dir.:\t\t\t\t" + getNetDir() +
                    "\n - Broadcast Dir.:\t\t\t" + getBrdDir() +
                    "\n\t· 1st Available Dir.:\t" + getFDir() +
                    "\n\t· Last Available Dir:\t" + getLDir();
        }

    }

    private String getString(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 24; i > 0; i-=8) {
            sb.append((n >>> i) & 255);
            sb.append(".");
        }
        sb.append(n & 255);
        return sb.toString();
    }

    public int netDir() {
        return (_ip&_mask);
    }

    public int broadcastDir() {
        // The mask is 255.255.255.255
        if (_mask == 0xffffffff) {
            return 0;
        }
        // Number of bits the mask has
        int nbits = 0;
        for (; nbits < 32; nbits++)
            if ((_mask << nbits) == 0)
                break;
        // Number of ips available
        int nips = 0;
        for (int i = 0; i < (32 - nbits); i++){
            nips <<= 1;
            nips |= 1;
        }

        return netDir() + nips;

    }

    public int firstDir() {
        return (netDir() + 1);
    }
    public int lastDir() {
        return (broadcastDir() - 1);
    }

    // Only works with nets multiple of 4
    public Net[] subnets(int nsub) {

        Net[] subnets = new Net[nsub];

        int nbsub = 0;
        for (; nbsub < 32; nbsub++)
            if(Math.pow(2, nbsub) >= nsub)
                break;

        int nbits = 0;
        for (; nbits < 32; nbits++)
            if ((get_mask() << nbits) == 0)
                break;

        for (int i = 0; i < subnets.length; i++) {
            subnets[i] = new Net();
            subnets[i].set_mask(get_mask() + ((nsub-1) << (32 - (nbits + nbsub))));
            if(i == 0) {
                subnets[i].set_ip(this.firstDir());
            } else {
                subnets[i].set_ip(subnets[i-1].broadcastDir() + 1);
            }

        }

        return subnets;

    }

}
