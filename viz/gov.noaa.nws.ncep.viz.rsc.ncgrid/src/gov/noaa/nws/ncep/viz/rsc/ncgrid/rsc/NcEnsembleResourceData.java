package gov.noaa.nws.ncep.viz.rsc.ncgrid.rsc;

import gov.noaa.nws.ncep.viz.common.dbQuery.NcConnector;
import gov.noaa.nws.ncep.viz.common.util.CommonDateFormatUtil;
import gov.noaa.nws.ncep.viz.rsc.ncgrid.dgdriv.GridDBConstants;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.raytheon.uf.common.dataplugin.PluginDataObject;
import com.raytheon.uf.common.dataquery.requests.DbQueryRequest;
import com.raytheon.uf.common.dataquery.requests.DbQueryRequest.OrderMode;
import com.raytheon.uf.common.dataquery.requests.RequestConstraint;
import com.raytheon.uf.common.dataquery.requests.RequestConstraint.ConstraintType;
import com.raytheon.uf.common.dataquery.responses.DbQueryResponse;
import com.raytheon.uf.common.status.IUFStatusHandler;
import com.raytheon.uf.common.status.UFStatus;
import com.raytheon.uf.common.status.UFStatus.Priority;
import com.raytheon.uf.common.time.DataTime;
import com.raytheon.uf.viz.core.exception.VizException;
import com.raytheon.uf.viz.core.requests.ThriftClient;
import com.raytheon.uf.viz.core.rsc.AbstractVizResource;
import com.raytheon.uf.viz.core.rsc.LoadProperties;

/**
 * Resource data for Ensemble grids
 * 
 * SOFTWARE HISTORY Date Ticket# Engineer Description ------------ ----------
 * ----------- -------------------------- 12/13/2011 G Hull Created. 04/02/2012
 * #606 G Hull added primaryModel for Ensem 09/11/2012 #743 Archana Added CLRBAR
 * 03/15/2012 G Hull added getComponentModels to support Ensemble component
 * cycle time query </pre>
 * 
 * @author ghull
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "NC-EnsembleResourceData")
public class NcEnsembleResourceData extends NcgridResourceData {

    private static final IUFStatusHandler statusHandler = UFStatus.getHandler(
            NcConnector.class, "DEFAULT");

    /**
     * Enum to map the cycle wild card strings in ensemble gdfile string with
     * the latest cycle date index returned by the query.
     */
    public enum CyclePlaceholder {
        CYCLE_1("C1", 0), CYCLE_2("C2", 1), CYCLE_3("C3", 2), CYCLE_4("C4", 3);

        public static int MAX_MODEL_CYCLES = values().length;

        private final String cycleString;

        private final int cycleIndex;

        private static Map<String, CyclePlaceholder> placeholderMapping;

        private CyclePlaceholder(String cycleString, int cycleIndex) {
            this.cycleString = cycleString;
            this.cycleIndex = cycleIndex;
        }

        private static void initMapping() {
            placeholderMapping = new HashMap<String, CyclePlaceholder>(
                    values().length);
            for (CyclePlaceholder placeholder : values()) {
                placeholderMapping.put(placeholder.cycleString, placeholder);
            }
        }

        private static Map<String, CyclePlaceholder> getCyclePlaceholderMap() {
            if (placeholderMapping == null) {
                initMapping();
            }
            return placeholderMapping;
        }

        public static int getCyclePlaceholderIndex(String placeholder) {
            if (getCyclePlaceholderMap().containsKey(placeholder)) {
                return getCyclePlaceholderMap().get(placeholder).cycleIndex;
            } else {
                return -1;
            }
        }

        public static String getCyclePlaceholderString(int placeholderIndex) {
            if (placeholderIndex < values().length && placeholderIndex >= 0) {
                return values()[placeholderIndex].cycleString;
            } else {
                return null;
            }
        }

        public static boolean containsCyclePlaceholders(String string) {
            for (CyclePlaceholder placeholder : values()) {
                if (string.contains(placeholder.cycleString)) {
                    return true;
                }
            }
            return false;
        }
    }

    @XmlElement
    protected String availableModels; // comma separated string

    public NcEnsembleResourceData() {
        super();
    }

    /**
     * Utility method to query for a models last
     * <code>CyclePlaceholder.MAX_MODEL_CYCLES</code> cycle times from the given
     * <code>queryRefDateTime</code>
     * 
     * @param modelName
     *            - name of model in model_info table. Use : to separate model
     *            name and ensemble id for ensemble members.
     * @param queryRefDateTime
     *            - DataTime reference time that will be formated using method
     *            <code>gov.noaa.nws.ncep.viz.common.util.CommonDateFormatUtil.dateToDbtimeQuery</code>
     * @return
     */
    public static Date[] queryLatestAvailCycleTimes(String modelName,
            DataTime queryRefDataTime) {
        return queryLatestAvailCycleTimes(modelName,
                CommonDateFormatUtil.dateToDbtimeQuery(queryRefDataTime
                        .getRefTime()), CyclePlaceholder.MAX_MODEL_CYCLES);
    }

    /**
     * Utility method to query for a models last <code>queryLimit</code> cycle
     * times from the given <code>queryRefDateTime</code>
     * 
     * @param modelName
     *            - name of model in model_info table. Use : to separate model
     *            name and ensemble id for ensemble members.
     * @param queryRefDateTime
     *            - string date in format "2011-10-09 06:20:00.0" Use method
     *            <code>gov.noaa.nws.ncep.viz.common.util.CommonDateFormatUtil.dateToDbtimeQuery</code>
     * @param queryLimit
     *            - resulting array limit. 0 and less will be unlimited
     * @return
     */
    public static Date[] queryLatestAvailCycleTimes(String modelName,
            String queryRefDateTime, int queryLimit) {
        Date[] availableCycles = new Date[0];
        String[] modelMember = modelName.split(":");
        HashMap<String, RequestConstraint> rcMap = new HashMap<String, RequestConstraint>();
        rcMap.put(GridDBConstants.PLUGIN_NAME, new RequestConstraint(
                GridDBConstants.GRID_TBL_NAME));
        rcMap.put(GridDBConstants.MODEL_NAME_QUERY, new RequestConstraint(
                modelMember[0], ConstraintType.EQUALS));
        if (modelMember.length > 1) {
            rcMap.put(GridDBConstants.ENSEMBLE_ID_QUERY, new RequestConstraint(
                    modelMember[1], ConstraintType.EQUALS));
        }
        rcMap.put(GridDBConstants.REF_TIME_QUERY, new RequestConstraint(
                queryRefDateTime, ConstraintType.LESS_THAN_EQUALS));
        DbQueryRequest request = new DbQueryRequest();
        request.addRequestField(GridDBConstants.REF_TIME_QUERY);
        request.setDistinct(true);
        request.setConstraints(rcMap);
        request.setOrderByField(GridDBConstants.REF_TIME_QUERY, OrderMode.DESC);
        if (queryLimit > 0) {
            request.setLimit(queryLimit);
        }

        try {
            DbQueryResponse response = (DbQueryResponse) ThriftClient
                    .sendRequest(request);
            // extract list of results
            List<Map<String, Object>> responseList = null;
            if (response != null) {
                responseList = response.getResults();
                int numCycles = responseList.size() > queryLimit
                        && queryLimit > 0 ? queryLimit : responseList.size();
                availableCycles = new Date[numCycles];
                for (int i = 0; i < numCycles; i++) {
                    availableCycles[i] = (Date) responseList.get(i).get(
                            GridDBConstants.REF_TIME_QUERY);
                }
            }
        } catch (VizException e) {
            // TODO Auto-generated catch block. Please revise as appropriate.
        }
        return availableCycles;
    }

    /**
     * Method to write out gdfile to xml and replace cycle strings with
     * respective wild cards.
     */
    public static String convertGdfileToWildcardString(
            String gdfileWithCycleStrings, DataTime queryRefDateTime) {
        StringBuilder gdFileBuilder = new StringBuilder(
                gdfileWithCycleStrings.length() * 2);
        String[] modelBlocks = gdfileWithCycleStrings.substring(
                gdfileWithCycleStrings.indexOf("{") + 1,
                gdfileWithCycleStrings.lastIndexOf("}")).split(",");
        for (int i = 0; i < modelBlocks.length; i++) {
            if (modelBlocks[i].contains("|")) {
                String percentage = modelBlocks[i].substring(0,
                        modelBlocks[i].indexOf("%") + 1);
                String[] modelCycleTime = modelBlocks[i].substring(
                        modelBlocks[i].indexOf("%") + 1).split("\\|");

                Date[] latestAvailCycles = queryLatestAvailCycleTimes(
                        modelCycleTime[0], queryRefDateTime);
                int cycleIndex = getLatestAvailCycleIndex(modelCycleTime[1],
                        latestAvailCycles);

                if (cycleIndex < 0) {
                    statusHandler
                            .handle(Priority.WARN,
                                    String.format(
                                            "The %s cycle is not available! Available cycles: %s\n",
                                            modelCycleTime[1],
                                            Arrays.toString(latestAvailCycles)));
                } else {
                    gdFileBuilder
                            .append(percentage)
                            .append(modelCycleTime[0])
                            .append("|")
                            .append(CyclePlaceholder
                                    .getCyclePlaceholderString(cycleIndex))
                            .append(",");
                }
            } else {
                gdFileBuilder.append(modelBlocks[i]).append(",");
            }
        }
        gdFileBuilder.deleteCharAt(gdFileBuilder.lastIndexOf(","));
        gdFileBuilder.insert(0, "{").append("}");
        return gdFileBuilder.toString();
    }

    private static int getLatestAvailCycleIndex(String cycleTimeStr,
            Date[] latestAvailCycles) {
        for (int i = 0; i < latestAvailCycles.length; i++) {
            if (cycleTimeStr.equals(CommonDateFormatUtil
                    .getCycleTimeString(latestAvailCycles[i]))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Method to read in gdfile from xml and replace cycle wild cards with
     * available cycle dates.
     */
    public static String convertGdfileToCycleTimeString(String gdfile,
            DataTime queryRefDateTime) {
        StringBuilder gdFileBuilder = new StringBuilder(gdfile.length() * 2);
        String[] modelBlocks = gdfile.substring(gdfile.indexOf("{") + 1,
                gdfile.lastIndexOf("}")).split(",");
        for (int i = 0; i < modelBlocks.length; i++) {
            String percentage = modelBlocks[i].substring(0,
                    modelBlocks[i].indexOf("%") + 1);
            String[] modelCycleTime = modelBlocks[i].substring(
                    modelBlocks[i].indexOf("%") + 1).split("\\|");
            Date[] latestAvailCycles = queryLatestAvailCycleTimes(
                    modelCycleTime[0], queryRefDateTime);

            if (CyclePlaceholder.containsCyclePlaceholders(modelBlocks[i])) {

                int cycleIndex = CyclePlaceholder
                        .getCyclePlaceholderIndex(modelCycleTime[1]);

                if (cycleIndex >= latestAvailCycles.length) {
                    statusHandler
                            .handle(Priority.WARN,
                                    String.format(
                                            "The %s cycle is not available! Available cycles: %s",
                                            modelCycleTime[1],
                                            Arrays.toString(latestAvailCycles)));
                } else {
                    gdFileBuilder
                            .append(percentage)
                            .append(modelCycleTime[0])
                            .append("|")
                            .append(CommonDateFormatUtil
                                    .getCycleTimeString(latestAvailCycles[cycleIndex]))
                            .append(",");
                }
            } else if (latestAvailCycles.length > 0) {
                gdFileBuilder.append(modelBlocks[i]).append(",");
            } else {
                statusHandler
                        .handle(Priority.WARN,
                                String.format(
                                        "No cycles found for model %s in gdfile block %s. Block will be ignored!",
                                        modelCycleTime[0], modelBlocks[i]));
            }
        }
        gdFileBuilder.deleteCharAt(gdFileBuilder.lastIndexOf(","));
        gdFileBuilder.insert(0, "{").append("}");
        return gdFileBuilder.toString();
    }

    @Override
    protected AbstractVizResource<?, ?> constructResource(
            LoadProperties loadProperties, PluginDataObject[] objects) {

        return new NcgridResource(this, loadProperties);
    }

    public String getAvailableModels() {
        return availableModels;
    }

    public void setAvailableModels(String availableModels) {
        this.availableModels = availableModels;
    }

    @Override
    public DataTime[] getAvailableTimes() throws VizException {
        DataTime[] availableTimes = super.getAvailableTimes();
        return availableTimes;
    }

    public String getGdfile() {
        return super.gdfile;
    }

    public void setGdfile(String gdfile) {
        super.gdfile = gdfile;
    }

    // Getter/Setters for the Attributes
    // These need to be here because the code that accesses the attributes using
    // reflection was not written to check for getter/setters in super classes.

    public String getCint() {
        return super.getCint();
    }

    public void setCint(String cint) {
        super.setCint(cint);
    }

    public String getGvcord() {
        return super.getGvcord();
    }

    public void setGvcord(String gvcord) {
        super.setGvcord(gvcord);
    }

    public String getGlevel() {
        return super.getGlevel();
    }

    public void setGlevel(String glevel) {
        super.setGlevel(glevel);
    }

    public String getGdpfun() {
        return super.getGdpfun();
    }

    public void setGdpfun(String gdpfun) {
        super.setGdpfun(gdpfun);
    }

    public String getSkip() {
        return super.getSkip();
    }

    public void setSkip(String skip) {
        super.setSkip(skip);
    }

    public String getFilter() {
        return super.getFilter();
    }

    public void setFilter(String filter) {
        super.setFilter(filter);
    }

    public String getScale() {
        return super.getScale();
    }

    public void setScale(String scale) {
        super.setScale(scale);
    }

    public String getTitle() {
        return super.getTitle();
    }

    public void setTitle(String title) {
        super.setTitle(title);
    }

    public String getType() {
        return super.getType();
    }

    public void setType(String type) {
        super.setType(type);
    }

    public String getLineAttributes() {
        return super.getLineAttributes();
    }

    public void setLineAttributes(String lineAttributes) {
        super.setLineAttributes(lineAttributes);
    }

    public String getColors() {
        return super.getColors();
    }

    public void setColors(String colors) {
        super.setColors(colors);
    }

    public String getMarker() {
        return super.getMarker();
    }

    public void setMarker(String marker) {
        super.setMarker(marker);
    }

    public int getGrdlbl() {
        return super.getGrdlbl();
    }

    public void setGrdlbl(int grdlbl) {
        super.setGrdlbl(grdlbl);
    }

    public String getFint() {
        return super.getFint();
    }

    public void setFint(String fint) {
        super.setFint(fint);
    }

    public String getFline() {
        return super.getFline();
    }

    public void setFline(String fline) {
        super.setFline(fline);
    }

    public String getWind() {
        return super.getWind();
    }

    public void setWind(String wind) {
        super.setWind(wind);
    }

    public String getHilo() {
        return super.getHilo();
    }

    public void setHilo(String hilo) {
        super.setHilo(hilo);
    }

    public String getHlsym() {
        return super.getHlsym();
    }

    public void setHlsym(String hlsym) {
        super.setHlsym(hlsym);
    }

    public String getClrbar() {
        return super.getClrbar();
    }

    public void setClrbar(String clrbar) {
        super.setClrbar(clrbar);
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        if (obj instanceof NcEnsembleResourceData == false) {
            return false;
        }

        NcEnsembleResourceData other = (NcEnsembleResourceData) obj;

        if (this.availableModels != null && other.availableModels == null) {
            return false;
        } else if (this.availableModels == null
                && other.availableModels != null) {
            return false;
        } else if (this.availableModels != null
                && this.availableModels.equals(other.availableModels) == false) {
            return false;
        }
        return true;
    }
}