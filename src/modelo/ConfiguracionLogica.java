package modelo;

/**
 * Configuracion logica de un equipo: hostname, IP, prefijo y VLAN.
 * Es inmutable: cada cambio crea un objeto nuevo, asi la pila puede
 * guardar la configuracion anterior sin que se modifique despues.
 * Incluye validaciones de red (IPv4, prefijo, VLAN, IP de host).
 * Autor: Kerly
 */
public final class ConfiguracionLogica {
    private final String hostname;
    private final String ip;
    private final int prefijo;
    private final int vlan;

    public ConfiguracionLogica(String hostname, String ip, int prefijo, int vlan) {
        this.hostname = hostname;
        this.ip = ip;
        this.prefijo = prefijo;
        this.vlan = vlan;
    }

    public String getHostname() {
        return hostname;
    }

    public String getIp() {
        return ip;
    }

    public int getPrefijo() {
        return prefijo;
    }

    public int getVlan() {
        return vlan;
    }

    /** Direccion de red calculada con la mascara (IP AND mascara). */
    public String getDireccionRed() {
        return aTexto(aEntero(ip) & mascara(prefijo));
    }

    public boolean mismosValores(ConfiguracionLogica otra) {
        return otra != null
                && hostname.equalsIgnoreCase(otra.hostname)
                && ip.equals(otra.ip)
                && prefijo == otra.prefijo
                && vlan == otra.vlan;
    }

    // ------------------ Validaciones estaticas ------------------

    /** Hostname: empieza con letra, luego letras, digitos o guion, max 20. */
    public static boolean esHostnameValido(String hostname) {
        return hostname != null && hostname.matches("[A-Za-z][A-Za-z0-9-]{0,19}");
    }

    /** IPv4 unicast: 4 octetos 0-255 y primer octeto entre 1 y 223. */
    public static boolean esIpValida(String ip) {
        if (ip == null) {
            return false;
        }
        String[] partes = ip.split("\\.", -1);
        if (partes.length != 4) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (!partes[i].matches("\\d{1,3}")) {
                return false;
            }
            int octeto = Integer.parseInt(partes[i]);
            if (octeto > 255) {
                return false;
            }
            if (i == 0 && (octeto < 1 || octeto > 223)) {
                return false;
            }
        }
        return true;
    }

    public static boolean esPrefijoValido(int prefijo) {
        return prefijo >= 8 && prefijo <= 30;
    }

    public static boolean esVlanValida(int vlan) {
        return vlan >= 1 && vlan <= 4094;
    }

    /** La IP no puede ser la direccion de red ni la de broadcast. */
    public static boolean esIpDeHost(String ip, int prefijo) {
        int valor = aEntero(ip);
        int mascara = mascara(prefijo);
        int parteHost = valor & ~mascara;
        return parteHost != 0 && parteHost != ~mascara;
    }

    private static int mascara(int prefijo) {
        return prefijo == 0 ? 0 : 0xFFFFFFFF << (32 - prefijo);
    }

    private static int aEntero(String ip) {
        String[] p = ip.split("\\.");
        return (Integer.parseInt(p[0]) << 24) | (Integer.parseInt(p[1]) << 16)
                | (Integer.parseInt(p[2]) << 8) | Integer.parseInt(p[3]);
    }

    private static String aTexto(int valor) {
        return ((valor >>> 24) & 255) + "." + ((valor >>> 16) & 255) + "."
                + ((valor >>> 8) & 255) + "." + (valor & 255);
    }

    @Override
    public String toString() {
        return hostname + " | " + ip + "/" + prefijo
                + " (red " + getDireccionRed() + ") | VLAN " + vlan;
    }
}
