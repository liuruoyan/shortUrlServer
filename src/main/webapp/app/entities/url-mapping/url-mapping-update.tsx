import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './url-mapping.reducer';
import { IUrlMapping } from 'app/shared/model/url-mapping.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IUrlMappingUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IUrlMappingUpdateState {
  isNew: boolean;
}

export class UrlMappingUpdate extends React.Component<IUrlMappingUpdateProps, IUrlMappingUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { urlMappingEntity } = this.props;
      const entity = {
        ...urlMappingEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/url-mapping');
  };

  render() {
    const { urlMappingEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="shortUrlServerApp.urlMapping.home.createOrEditLabel">
              <Translate contentKey="shortUrlServerApp.urlMapping.home.createOrEditLabel">Create or edit a UrlMapping</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : urlMappingEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="url-mapping-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="url-mapping-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="urlLongLabel" for="url-mapping-urlLong">
                    <Translate contentKey="shortUrlServerApp.urlMapping.urlLong">Url Long</Translate>
                  </Label>
                  <AvField id="url-mapping-urlLong" type="text" name="urlLong" />
                </AvGroup>
                <AvGroup>
                  <Label id="urlShortLabel" for="url-mapping-urlShort">
                    <Translate contentKey="shortUrlServerApp.urlMapping.urlShort">Url Short</Translate>
                  </Label>
                  <AvField id="url-mapping-urlShort" type="text" name="urlShort" />
                </AvGroup>
                <AvGroup>
                  <Label id="typeLabel" for="url-mapping-type">
                    <Translate contentKey="shortUrlServerApp.urlMapping.type">Type</Translate>
                  </Label>
                  <AvField id="url-mapping-type" type="string" className="form-control" name="type" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/url-mapping" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  urlMappingEntity: storeState.urlMapping.entity,
  loading: storeState.urlMapping.loading,
  updating: storeState.urlMapping.updating,
  updateSuccess: storeState.urlMapping.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UrlMappingUpdate);
