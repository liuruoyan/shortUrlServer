import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UrlMapping from './url-mapping';
import UrlMappingDetail from './url-mapping-detail';
import UrlMappingUpdate from './url-mapping-update';
import UrlMappingDeleteDialog from './url-mapping-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UrlMappingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UrlMappingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UrlMappingDetail} />
      <ErrorBoundaryRoute path={match.url} component={UrlMapping} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={UrlMappingDeleteDialog} />
  </>
);

export default Routes;
