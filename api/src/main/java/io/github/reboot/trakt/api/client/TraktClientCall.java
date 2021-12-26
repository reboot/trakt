package io.github.reboot.trakt.api.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

class TraktClientCall {

    private final StringBuilder file;

    private boolean firstParameter = true;

    TraktClientCall(String path) {
        file = new StringBuilder(path);
    }

    void append(String str) {
        if (!firstParameter) {
            throw new IllegalStateException("Can not add to the path after adding parameters.");
        }
        file.append(str);
    }

    /**
     * Appends a named parameter to the end of the URL string builder.
     *
     * @param name The name of the parameter.
     * @param value The value of the parameter.
     */
    void appendQueryParameter(String name, String value) {
        String valueEncoded;
        try {
            valueEncoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (firstParameter) {
            file.append("?");
        } else {
            file.append("&");
        }
        file.append(name);
        file.append("=");
        file.append(valueEncoded);

        firstParameter = false;
    }

    /**
     * Appends the request parameters to end of the URL string builder.
     *
     * @param parameters The request parameters that should be appended to the
     *        URL.
     */
    void appendRequestParameters(RequestParameter... parameters) {
        for (RequestParameter parameter : parameters) {
            if (parameter instanceof PaginationParameter) {
                PaginationParameter paginationParameter = (PaginationParameter) parameter;

                Integer page = paginationParameter.getPage();
                if (page != null) {
                    String value = Integer.toString(page);
                    appendQueryParameter("page", value);
                }
                Integer limit = paginationParameter.getLimit();
                if (limit != null) {
                    String value = Integer.toString(limit);
                    appendQueryParameter("limit", value);
                }
            } else if (parameter instanceof ExtendedInfoParameter) {
                ExtendedInfoParameter extendedInfoParameter = (ExtendedInfoParameter) parameter;

                StringBuilder value = new StringBuilder();
                for (ExtendedInfoParameter.Level extended : extendedInfoParameter.getExtended()) {
                    if (value.length() > 0) {
                        value.append(",");
                    }
                    String level;
                    switch (extended) {
                    case FULL:
                        level = "full";
                        break;
                    case METADATA:
                        level = "metadata";
                        break;
                    default:
                        throw new AssertionError(extended);
                    }
                    value.append(level);
                }
                appendQueryParameter("extended", value.toString());
            } else if (parameter instanceof FiltersParameter) {
                FiltersParameter filtersParameters = (FiltersParameter) parameter;

                appendQueryParameter("query", filtersParameters.getQuery());
            }
        }
    }

    String toFile() {
        return file.toString();
    }

}
