import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IUrlMapping, defaultValue } from 'app/shared/model/url-mapping.model';

export const ACTION_TYPES = {
  FETCH_URLMAPPING_LIST: 'urlMapping/FETCH_URLMAPPING_LIST',
  FETCH_URLMAPPING: 'urlMapping/FETCH_URLMAPPING',
  CREATE_URLMAPPING: 'urlMapping/CREATE_URLMAPPING',
  UPDATE_URLMAPPING: 'urlMapping/UPDATE_URLMAPPING',
  DELETE_URLMAPPING: 'urlMapping/DELETE_URLMAPPING',
  RESET: 'urlMapping/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUrlMapping>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type UrlMappingState = Readonly<typeof initialState>;

// Reducer

export default (state: UrlMappingState = initialState, action): UrlMappingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_URLMAPPING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_URLMAPPING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_URLMAPPING):
    case REQUEST(ACTION_TYPES.UPDATE_URLMAPPING):
    case REQUEST(ACTION_TYPES.DELETE_URLMAPPING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_URLMAPPING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_URLMAPPING):
    case FAILURE(ACTION_TYPES.CREATE_URLMAPPING):
    case FAILURE(ACTION_TYPES.UPDATE_URLMAPPING):
    case FAILURE(ACTION_TYPES.DELETE_URLMAPPING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_URLMAPPING_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_URLMAPPING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_URLMAPPING):
    case SUCCESS(ACTION_TYPES.UPDATE_URLMAPPING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_URLMAPPING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/url-mappings';

// Actions

export const getEntities: ICrudGetAllAction<IUrlMapping> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_URLMAPPING_LIST,
    payload: axios.get<IUrlMapping>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IUrlMapping> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_URLMAPPING,
    payload: axios.get<IUrlMapping>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IUrlMapping> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_URLMAPPING,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IUrlMapping> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_URLMAPPING,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUrlMapping> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_URLMAPPING,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
