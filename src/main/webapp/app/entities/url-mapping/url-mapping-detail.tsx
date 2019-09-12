import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './url-mapping.reducer';
import { IUrlMapping } from 'app/shared/model/url-mapping.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUrlMappingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class UrlMappingDetail extends React.Component<IUrlMappingDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { urlMappingEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="shortUrlServerApp.urlMapping.detail.title">UrlMapping</Translate> [<b>{urlMappingEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="urlLong">
                <Translate contentKey="shortUrlServerApp.urlMapping.urlLong">Url Long</Translate>
              </span>
            </dt>
            <dd>{urlMappingEntity.urlLong}</dd>
            <dt>
              <span id="urlShort">
                <Translate contentKey="shortUrlServerApp.urlMapping.urlShort">Url Short</Translate>
              </span>
            </dt>
            <dd>{urlMappingEntity.urlShort}</dd>
            <dt>
              <span id="type">
                <Translate contentKey="shortUrlServerApp.urlMapping.type">Type</Translate>
              </span>
            </dt>
            <dd>{urlMappingEntity.type}</dd>
          </dl>
          <Button tag={Link} to="/entity/url-mapping" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/url-mapping/${urlMappingEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ urlMapping }: IRootState) => ({
  urlMappingEntity: urlMapping.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UrlMappingDetail);
